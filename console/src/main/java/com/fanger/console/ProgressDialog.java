package com.fanger.console;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ProgressDialog extends Stage {

    private final ProgressBar pb;

    private Label label;



    private Button button;

    ProgressDialog() {
        Group group = new Group();
        this.setTitle("安装桌面版IDE");
        Scene scene = new Scene(group, 400, 120);
        this.setScene(scene);
        pb = new ProgressBar();
        pb.setMinWidth(380);
        button = new Button("关闭");
        button.setMinWidth(100);
        button.setDisable(true);
        button.setOnAction(event -> {
            this.close();
        });
        label = new Label("桌面版IDE安装中...");
        final VBox hb = new VBox();
        hb.setSpacing(20);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(label, pb, button);
        scene.setRoot(hb);
    }

    public ProgressBar getPb() {
        return pb;
    }

    public Label getLabel() {
        return label;
    }

    public Button getButton() {
        return button;
    }
}
