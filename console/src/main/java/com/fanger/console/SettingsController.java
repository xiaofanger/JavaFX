package com.fanger.console;

import com.fanger.util.JFXUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class SettingsController {

    @FXML
    private TextField gradlePathField;

    public void onCloseBtnClick(ActionEvent event) {

    }

    /**
     * 打开文件夹选择器，获取选择的文件路径
     *
     * @param event
     */
    public void onOpenDir(ActionEvent event) {
        JFXUtil.onOpenDir(gradlePathField);
    }


    public void installSenchaCmd(ActionEvent event) {

        System.out.println("------ 安装 CMD ------");

    }

}
