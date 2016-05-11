package ru.khasang.cachoeira.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AboutWindowView implements IView {
    private Stage stage;

    @Override
    public void createView() {
        VBox box = new VBox();
        box.setPadding(new Insets(10));
        Text text = new Text("Cachoeira");
        text.setFont(Font.font(100));
        box.getChildren().add(text);
        box.setAlignment(Pos.CENTER);
        stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setWidth(500);
        stage.setHeight(300);
        stage.setScene(new Scene(box));
        stage.show();
    }

    @Override
    public Stage getStage() {
        return stage;
    }
}
