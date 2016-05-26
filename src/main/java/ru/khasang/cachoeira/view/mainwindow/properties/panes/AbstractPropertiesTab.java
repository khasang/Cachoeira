package ru.khasang.cachoeira.view.mainwindow.properties.panes;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public abstract class AbstractPropertiesTab extends Tab {
    private final static double PANE_WIDTH = 299;
    protected VBox pane;

    public AbstractPropertiesTab() {
        pane = new VBox();
        this.setContent(createScrollPaneToPropertyPane(pane));
    }

    public void addNewPropertiesTitledModule(Node module, String titleOfModule) {
        pane.getChildren().add(new TitledPane(titleOfModule, module));
        pane.setPrefWidth(PANE_WIDTH);
    }

    private ScrollPane createScrollPaneToPropertyPane(Node pane) {
        ScrollPane scrollPane = new ScrollPane(pane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        return scrollPane;
    }
}
