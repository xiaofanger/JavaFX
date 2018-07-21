package com.fanger.console;

import com.fanger.event.ExitEvent;
import com.fanger.event.StartAndStopEvent;
import com.fanger.spring.InitApplicationContext;
import com.fanger.util.JFXUtil;
import com.fanger.util.RemoteDownload;
import com.google.common.eventbus.EventBus;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 事件处理类
 */
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    @FXML
    private TextField jdkPathField;
    @FXML
    private TextField urlField;
    @FXML
    private Button openBrowser;
    @FXML
    private Button openIDE;
    @FXML
    private Button copyBtn;
    @FXML
    private Button viewLog;
    @FXML
    private ImageView closeBtn;
    @FXML
    private ImageView minBtn;
    @FXML
    private TextField serverStatus;
    @FXML
    private TextField serverPort;
    @FXML
    private TextField ideaPortField;
    @FXML
    private Button startAndStopBtn;

    public int SERVER_PORT = 9996;

    /**
     * 打开文件夹选择器，获取选择的文件路径
     *
     * @param event
     */
    public void onOpenDir(ActionEvent event) {
        JFXUtil.onOpenDir(jdkPathField);
    }

    /**
     * 复制URL地址到剪切板
     *
     * @param event
     */
    public void onCopyUrl(ActionEvent event) {
        // 创建剪切板
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        // 获取剪切内容
        clipboardContent.putString(urlField.getText());
        // 将剪切内容设置至剪切板
        clipboard.setContent(clipboardContent);
    }

    /**
     * 监听URL输入字段的长度，动态改变Copy按钮的状态
     *
     * @param event
     */
    public void onTextChanegd(Event event) {
        String text = urlField.getText();
        if (text.length() > 0) {
            copyBtn.setDisable(false);
        } else {
            copyBtn.setDisable(true);
        }
    }

    /**
     * 高级页面
     */
    private Settings settings;
    /**
     * 打开高级配置页面
     *
     * @param event
     */
    public void onSettingPane(ActionEvent event) {
        if (settings == null) {
            settings = new Settings();
        }
        settings.show();
    }

    public void openLogDir(ActionEvent event) throws IOException {
        File logs = new File( "logs");
        Runtime rt = Runtime.getRuntime();
        rt.exec("explorer /e,/root,\""+logs.getAbsolutePath()+"\"" );
    }

    /**
     * 调用 Chrome 浏览器打开url
     *
     * @param event
     */
    public void openBrowserToUrl(ActionEvent event) {
//        String browserPath = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe";
        String url = urlField.getText();
        String str = "cmd /c start chrome " + url;
        if (url.length() > 1) {
            if (Desktop.isDesktopSupported()) { //判断是否支持Desktop扩展,如果支持则进行下一步
                try {
                    Runtime.getRuntime().exec(str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("提示");
            alert.setHeaderText("该URL地址为空，或者URL地址格式错误！");
            alert.setContentText("请输入正确的URL地址。");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    alert.close();
                }
            });
        }
    }

    //创建任务
    private Task createTask(){
        return new Task<Void>(){
            @Override protected Void call() throws Exception {
                String url = null;
                File targetFile = new File(".", "ide");
                try {
                    new RemoteDownload() {
                        @Override
                        public void onProgress(float progress, long total) {
                            updateProgress((int)progress, 100);
                            if ((int)progress == 100) {
                                Platform.runLater(() -> {
                                    progressDialog.getLabel().setText("安装完成，重新点击启动桌面版IDE。");
                                    progressDialog.getButton().setDisable(false);
                                });
                            }
                        }
                    }.downloadFile(targetFile, url, true);
                } catch (Exception ex) {
                    Platform.runLater(() -> {
                        progressDialog.hide();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("提示");
                        alert.setHeaderText("IDE安装失败！");
                        alert.setContentText(ex.toString());
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                alert.close();
                            }
                        });
                    });
                }
                return null;
            }
        };
    }
    private ProgressDialog progressDialog;

    /**
     * 打开桌面版IDE
     */
    public void openDesktopIDE() {
        // 获取IDE配置文件
        File config = new File(".", "ide/resources/app/config.json");
        File exe = new File(".", "ide/JdStudio.exe");
        if (!config.exists() || !exe.exists()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("提示");
            alert.setHeaderText("桌面版IDE未安装！");
            alert.setContentText("是否下载安装桌面版IDE？");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    alert.close();
                    new Thread(new Task<Void>() {
                        @Override
                        protected Void call() {
                            return null;
                        }
                        @Override
                        protected void succeeded() {
                            // 显示等待进度条
                            progressDialog = new ProgressDialog();
                            ProgressBar bar = progressDialog.getPb();
                            Task task = createTask();
                            bar.progressProperty().bind(task.progressProperty());
                            progressDialog.show();
                            Thread thread = new Thread(task);
                            thread.setDaemon(true);
                            thread.start();
                            super.succeeded();
                        }
                    }).start();
                }
                if (response == ButtonType.CANCEL) {
                    alert.close();
                }
            });
        }
    }

    /**
     * 服务按钮
     */
    public void startServer(ActionEvent event) {
        ctx = InitApplicationContext.createInstance();
        EventBus eventBus = ctx.getBean(EventBus.class);
        eventBus.post(new StartAndStopEvent());
    }

    private ExecutorService executor = Executors.newFixedThreadPool(10);


    /**
     * Spring应用上下文
     */
    private AnnotationConfigApplicationContext ctx;

    /**
     * 退出按钮响应
     */
    public void onExitButtonAction() {
        ctx = InitApplicationContext.createInstance();
        EventBus eventBus = ctx.getBean(EventBus.class);
        eventBus.post(new ExitEvent());
    }
}
