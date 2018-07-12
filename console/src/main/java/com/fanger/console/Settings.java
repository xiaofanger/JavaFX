package com.fanger.console;

import com.fanger.util.JFXUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class Settings extends Stage {

    private double x1;      // 鼠标在屏幕中的位置
    private double y1;

    private double xStage;  // 窗口在屏幕中的位置
    private double yStage;

    private GridPane gridPane;
    private ImageView closeBtn;     // 关闭按钮
    private Button cancelBtn;  //取消按钮

    private static Logger logger = LoggerFactory.getLogger(Settings.class);

    Settings() {
        init();
    }

    public void init() {
        this.initStyle(StageStyle.UNDECORATED); // 去掉标题栏
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader()
                    .getResource("StudioSettingsView.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setTitle("高级设置");
        this.setScene(new Scene(root, 800, 460));
        this.setMaxWidth(900);
        this.setMaxHeight(580);
        this.setMinWidth(800);
        this.setMinHeight(520);

        // 设置场景的初始位置
        this.setX(this.getX());
        this.setY(this.getY());

        // 标题栏实现窗口拖动
        gridPane = (GridPane) root.lookup("#top");
        setWindowDragged(this, gridPane);

        // 响应关闭按钮点击事件
        closeBtn = (ImageView) root.lookup("#closeBtn");
        closeBtn.setOnMouseClicked(event -> {
            this.hide();
        });
        // 相应取消按钮点击事件
        cancelBtn = (Button) root.lookup("#cancelBtn");
        cancelBtn.setOnAction(event -> {
            this.hide();
        });
    }

    /**
     * 自定义窗口拖动
     *
     * @param stage    场景窗口
     * @param gridPane 响应拖动的组件
     */
    private void setWindowDragged(Stage stage, GridPane gridPane) {
        JFXUtil.setWindowDragged(stage, gridPane);
    }

}
