package ru.khasang.cachoeira.view.mainwindow.properties;

import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public abstract class AbstractPropertiesPane extends VBox {
    private final static double PANE_WIDTH = 299;

    public void addNewPropertiesTitledModule(Node module, String titleOfModule) {
        this.getChildren().add(new TitledPane(titleOfModule, module));
        this.setPrefWidth(PANE_WIDTH);
    }
}
