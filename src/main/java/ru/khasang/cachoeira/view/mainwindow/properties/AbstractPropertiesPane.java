package ru.khasang.cachoeira.view.mainwindow.properties;

import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class AbstractPropertiesPane extends VBox {
    public void addNewPropertiesTitledModule(Node module, String titleOfModule) {
        getChildren().add(new TitledPane(titleOfModule, module));
    }
}
