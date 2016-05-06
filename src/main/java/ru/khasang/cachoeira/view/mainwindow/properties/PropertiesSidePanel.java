package ru.khasang.cachoeira.view.mainwindow.properties;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import ru.khasang.cachoeira.view.mainwindow.properties.panes.AbstractPropertiesPane;

public class PropertiesSidePanel extends AbstractPropertiesSidePanel {
    private final AbstractPropertiesPane projectPropertiesPane;
    private final AbstractPropertiesPane taskPropertiesPane;
    private final AbstractPropertiesPane resourcePropertiesPane;

    public PropertiesSidePanel(AbstractPropertiesPane projectPropertiesPane,
                               AbstractPropertiesPane taskPropertiesPane,
                               AbstractPropertiesPane resourcePropertiesPane) {
        this.projectPropertiesPane = projectPropertiesPane;
        this.taskPropertiesPane = taskPropertiesPane;
        this.resourcePropertiesPane = resourcePropertiesPane;
    }

    @Override
    public void createPanes() {
        Tab projectTab = new Tab("Project", createScrollPaneToPropertyPane(projectPropertiesPane));
        projectTab.setGraphic(new ImageView(getClass().getResource("/img/ic_project.png").toExternalForm()));
        Tab taskTab = new Tab("Task", createScrollPaneToPropertyPane(taskPropertiesPane));
        taskTab.setGraphic(new ImageView(getClass().getResource("/img/ic_task.png").toExternalForm()));
        Tab resourceTab = new Tab("Resource", createScrollPaneToPropertyPane(resourcePropertiesPane));
        resourceTab.setGraphic(new ImageView(getClass().getResource("/img/ic_resource.png").toExternalForm()));

        this.getTabs().addAll(projectTab, taskTab, resourceTab);
        this.setPrefWidth(310);
        this.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    }

    private ScrollPane createScrollPaneToPropertyPane(Node pane) {
        ScrollPane scrollPane = new ScrollPane(pane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        return scrollPane;
    }

    @Override
    public AbstractPropertiesPane getProjectPropertiesPane() {
        return projectPropertiesPane;
    }

    @Override
    public AbstractPropertiesPane getTaskPropertiesPane() {
        return taskPropertiesPane;
    }

    @Override
    public AbstractPropertiesPane getResourcePropertiesPane() {
        return resourcePropertiesPane;
    }
}