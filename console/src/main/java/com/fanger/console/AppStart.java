package com.fanger.console;

import com.fanger.event.ExitEvent;
import com.fanger.event.StartAndStopEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.AccessibleAction;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Admin
 */
public class AppStart extends Application {

    private static Logger logger = LoggerFactory.getLogger("AppStart");

    private AnnotationConfigApplicationContext ctx;
    private EventBus eventBus;
    private MainController mainController;

    private final int CLICK_COUNT = 2;
    private final String MENU_OPEN_IDE = "桌面版";
    private final String MENU_OPEN_CONSOLE = "打开控制台";
    private final String MENU_EXIT = "退出";

    private Button openDesktopIDE;

    private Parent root;

    @Override
    public void init() throws Exception {
        super.init();
        logger.debug("应用启动，初始化资源");
        ctx = InitApplicationContext.createInstance();
        //启动当前的应用上下文
        ctx.start();
        // JVM退出时结束当前的应用上下文
        ctx.registerShutdownHook();

        mainController = ctx.getBean(MainController.class);
        eventBus = ctx.getBean(EventBus.class);

        eventBus.register(this);
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        logger.debug("应用退出，销毁资源");
    }

    private void exit() {
        eventBus.unregister(this);
        Platform.exit();
        System.exit(0);
    }

    @Subscribe
    public void actionExit(ExitEvent event) {
        logger.debug("处理事件");
        try {
            this.stop();
            this.exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否启动（在运行中）
     */
    private boolean isStart = false;

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    @Subscribe
    public void actionStartAndStop(StartAndStopEvent event) {
        if (this.isStart()) {
            Button stop = (Button) root.lookup("#startAndStopBtn");
            stop.setGraphic(new ImageView(
                    new javafx.scene.image.Image(getClass().getClassLoader().getResourceAsStream("images/start.png"))
            ));
            stop.setContentDisplay(ContentDisplay.LEFT);
            stop.setText("启动");
            this.setStart(false);
        } else {
            Button start = (Button) root.lookup("#startAndStopBtn");
            start.setGraphic(new ImageView(
                    new javafx.scene.image.Image(getClass().getClassLoader().getResourceAsStream("images/stop.png"))
            ));
            start.setContentDisplay(ContentDisplay.LEFT);
            start.setText("停止");
            this.setStart(true);
        }
    }

    private void initResource() throws IOException {
        // 获取当前屏幕的分辨率
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        if (dimension.getWidth() <= 1366.0 && dimension.getHeight() <= 768.0) {
            //用于小分辨率的屏幕
            root = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("LowStudioConsoleView.fxml")));
        } else {
            root = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("StudioConsoleView.fxml")));
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.initResource();
        Scene scene;
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        if (dimension.getWidth() <= 1366.0 && dimension.getHeight() <= 768.0) {
            //用于小分辨率的屏幕
            scene = new Scene(root, 600, 400);
            primaryStage.setMaxWidth(760);
            primaryStage.setMaxHeight(520);
            primaryStage.setMinWidth(620);
            primaryStage.setMinHeight(440);
        } else {
            scene = new Scene(root, 800, 460);
            primaryStage.setMaxWidth(860);
            primaryStage.setMaxHeight(580);
            primaryStage.setMinWidth(620);
            primaryStage.setMinHeight(500);
        }
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("StudioConsole.css")).toExternalForm());
        primaryStage.setScene(scene);

        primaryStage.setTitle("JdYun Studio 1.0.0");
        setWindowIcon(primaryStage); //设置窗体图标

        // 初始加载运行时JDK路劲
        TextField jdkPathField = (TextField) root.lookup("#jdkPathField");
        String msg = System.getProperty("java.home");
        jdkPathField.setText(msg.substring(0, msg.lastIndexOf("\\")));

        primaryStage.show();
        Platform.setImplicitExit(false); // 阻止退出的默认行为
        enableTray(primaryStage);  // 开启系统托盘

        //退出按钮
        Button exitBtn = (Button) root.lookup("#exitBtn");
        exitBtn.setGraphic(new ImageView(
                new javafx.scene.image.Image(getClass().getClassLoader().getResourceAsStream("images/exit.png"))
        ));
        exitBtn.setContentDisplay(ContentDisplay.LEFT);
        //文件夹
        Button dirBtn = (Button) root.lookup("#selectFileBtn");
        dirBtn.setGraphic(new ImageView(
                new javafx.scene.image.Image(getClass().getClassLoader().getResourceAsStream("images/dir.png"))
        ));
        dirBtn.setContentDisplay(ContentDisplay.LEFT);
        //启动
        Button startBtn = (Button) root.lookup("#startAndStopBtn");
        startBtn.setGraphic(new ImageView(
                new javafx.scene.image.Image(getClass().getClassLoader().getResourceAsStream("images/start.png"))
        ));
        startBtn.setContentDisplay(ContentDisplay.LEFT);
        //查看日志
        Button workBtn = (Button) root.lookup("#viewLog");
        workBtn.setGraphic(new ImageView(
                new javafx.scene.image.Image(getClass().getClassLoader().getResourceAsStream("images/work.png"))
        ));
        workBtn.setContentDisplay(ContentDisplay.LEFT);
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception ex) {
            logger.error(ex.getMessage()+Arrays.toString(ex.getStackTrace()));
        }
    }

    /**
     * 设置窗口图标
     */
    private void setWindowIcon(Stage stage) {
        InputStream in = getClass().getClassLoader().getResourceAsStream("images/logo.png");
        javafx.scene.image.Image image = new javafx.scene.image.Image(in);
        stage.getIcons().add(image);
    }



    /**
     * 开启系统托盘
     *
     * @param stage
     */
    private void enableTray(final Stage stage) {
        if (SystemTray.isSupported()) {
            logger.info("系统支持托盘");
            // 创建系统托盘
            SystemTray sysTray = SystemTray.getSystemTray();
            // 创建托盘图标
            Image image = null;
            try {
                image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("images/tuopanlogo.png")));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Font font = new Font("隶书", 10, 14);

            // 创建托盘菜单
            PopupMenu popupMenu = new PopupMenu();
            popupMenu.setFont(font);
            MenuItem openIDE = new MenuItem("桌面版");
            MenuItem openConsole = new MenuItem("打开控制台");
            MenuItem exit = new MenuItem("退出");
            MenuItem about = new MenuItem("关于");
            MenuItem checkUpdate = new MenuItem("检查更新");

            // 托盘图标
            TrayIcon trayIcon = new TrayIcon(image, "JdYunStudio", popupMenu);

            // 鼠标事件监听
            trayIcon.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == CLICK_COUNT) {
                        if (stage.isShowing()) {
                            Platform.runLater(stage::hide);
                        } else {
                            Platform.runLater(stage::show);
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            // 监听菜单事件
            ActionListener menuItemAction = new ActionListener() {
                private void run() {
                    mainController.onExitButtonAction();
                }

                @Override
                public void actionPerformed(ActionEvent e) {
                    MenuItem item = (MenuItem) e.getSource();
                    if (item.getLabel().equals(MENU_OPEN_IDE)) {
                        Platform.runLater(() -> {
                            stage.toFront();
                            openDesktopIDE = (Button) root.lookup("#openIDE");
                            if(openDesktopIDE.isDisabled()) {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("提示");
                                alert.setHeaderText("启动桌面版IDE失败！");
                                alert.setContentText("Studio服务未启动，请先启动Studio服务。");
                                alert.showAndWait().ifPresent(response -> {
                                    if (response == ButtonType.OK) {
                                        alert.close();
                                    }
                                });
                            } else {
                                mainController.openDesktopIDE();
                            }
                        });
                    }
                    if (item.getLabel().equals(MENU_OPEN_CONSOLE)) {
                        Platform.runLater(() -> {
                            if (!stage.isShowing()) { // 是否隐藏
                                stage.show();
                            } else if (stage.isIconified()) { // 是否获得焦点
                                stage.setIconified(false);
                            } else if(!stage.isFocused()){ //闪烁
                                stage.requestFocus();
                            } else if (stage.isIconified() && !stage.isShowing()) {
                                stage.show();
                            } else {
                                stage.toFront(); // 将窗口带到前景
                            }
                        });
                    }
                    if (item.getLabel().equals(MENU_EXIT)) {
                        Platform.runLater(this::run);
                    }
                }
            };
            openIDE.addActionListener(menuItemAction);
            openConsole.addActionListener(menuItemAction);
            exit.addActionListener(menuItemAction);
            checkUpdate.addActionListener(menuItemAction);
            about.addActionListener(menuItemAction);
            popupMenu.insert(openIDE, 0);
            popupMenu.insert(openConsole, 1);
            popupMenu.insertSeparator(2);
            popupMenu.insert(checkUpdate, 3);
            popupMenu.insert(about, 4);
            popupMenu.insert(exit, 5);

            try {
                sysTray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }

        } else {
            logger.info("系统不支持托盘");
            Platform.exit();
        }
    }
}
