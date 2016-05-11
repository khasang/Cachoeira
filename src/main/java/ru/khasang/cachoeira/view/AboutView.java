package ru.khasang.cachoeira.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AboutView implements IView {
    private Stage stage;

    @Override
    public void createView() {
        VBox pane = new VBox();
        pane.setPadding(new Insets(10));
        pane.getChildren().add(new Text("Cachoeira"));
        pane.setAlignment(Pos.TOP_CENTER);
        stage = new Stage(StageStyle.DECORATED);
        stage.setWidth(500);
        stage.setHeight(500);
        stage.setScene(new Scene(pane));
        stage.show();
    }

    @Override
    public Stage getStage() {
        return stage;
    }
}
