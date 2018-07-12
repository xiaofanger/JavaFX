package com.fanger.util;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class JFXUtil {

    private static double x1;      // 鼠标在屏幕中的位置
    private static double y1;

    private static double xStage;  // 窗口在屏幕中的位置
    private static double yStage;

    /**
     * 自定义窗口拖动
     *
     * @param stage    场景窗口
     * @param gridPane 响应拖动的组件
     */
    public static void setWindowDragged(Stage stage, GridPane gridPane) {

        Scene scene = stage.getScene();
        scene.setOnDragEntered(null);

        //按下鼠标后，记录当前鼠标的坐标
        gridPane.setOnMousePressed(event -> {
            event.consume();
            x1 =event.getScreenX();
            y1 =event.getScreenY();
            xStage = stage.getX();
            yStage = stage.getY();
        });
        gridPane.setOnMouseDragged(event -> {
            event.consume();
            stage.setX(xStage + event.getScreenX() - x1);
            stage.setY(yStage + event.getScreenY() - y1);
        });
    }


    /**
     * 文件选择
     * @param field
     */
    public static void onOpenDir(TextField field) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        Stage stage = new Stage();
        dirChooser.setTitle("选择JDK路径");
        String fileDirStr = field.getText();
        if (fileDirStr.length() > 2) {
            File initFileDir = new File(fileDirStr); // 定位初始文件位置
            dirChooser.setInitialDirectory(initFileDir);
        }
        File dir = dirChooser.showDialog(stage);
        if (dir != null) {
            String path = dir.getAbsolutePath();
            field.setText(path);
        }
    }
}
