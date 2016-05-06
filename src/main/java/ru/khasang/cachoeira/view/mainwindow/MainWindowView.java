package ru.khasang.cachoeira.view.mainwindow;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.khasang.cachoeira.model.*;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.diagram.TableAndGanttPane;
import ru.khasang.cachoeira.view.mainwindow.menubar.AbstractMenuBar;
import ru.khasang.cachoeira.view.mainwindow.properties.*;

public class MainWindowView {
    private final MainWindowController controller;
    private final IProject project;
    private final AbstractMenuBar menuBar;
    private final AbstractPropertiesSidePanel propertiesSidePanel;
    private final TableAndGanttPane tasksPane;
    private final TableAndGanttPane resourcesPane;

    private Stage stage;

    public MainWindowView(MainWindowController controller,
                          IProject project,
                          AbstractMenuBar menuBar,
                          AbstractPropertiesSidePanel propertiesSidePanel,
                          TableAndGanttPane tasksPane,
                          TableAndGanttPane resourcesPane) {
        this.controller = controller;
        this.project = project;
        this.menuBar = menuBar;
        this.propertiesSidePanel = propertiesSidePanel;
        this.tasksPane = tasksPane;
        this.resourcesPane = resourcesPane;
    }

    public void createView() {
        BorderPane borderPane = new BorderPane();
//        borderPane.getStylesheets().add(getClass().getResource("/css/startwindow.css").toExternalForm());
        borderPane.setTop(createMenuBar());
        borderPane.setCenter(createGanttPlanLayout());
        borderPane.setRight(createPropertiesSidePanel());

        stage = new Stage();
        stage.setHeight(controller.getHeightOfWindow());
        stage.setWidth(controller.getWidthOfWindow());
        stage.setScene(new Scene(borderPane));
        stage.show();
        stage.setMaximized(controller.getIsMaximized());
        stage.titleProperty().bind(project.nameProperty());
    }

    private Node createGanttPlanLayout() {
        tasksPane.createPane();
        resourcesPane.createPane();

        Tab tasksTab = new Tab("Tasks", tasksPane);
        tasksTab.setClosable(false);
        Tab resourcesTab = new Tab("Resources", resourcesPane);
        resourcesTab.setClosable(false);
        return new TabPane(tasksTab, resourcesTab);
    }

    private AbstractMenuBar createMenuBar() {
        menuBar.createMenu();
        return menuBar;
    }

    private AbstractPropertiesSidePanel createPropertiesSidePanel() {
        propertiesSidePanel.createPanes();
        return propertiesSidePanel;
    }

    public Stage getStage() {
        return stage;
    }
}
