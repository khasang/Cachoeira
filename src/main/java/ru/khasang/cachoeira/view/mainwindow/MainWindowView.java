package ru.khasang.cachoeira.view.mainwindow;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.model.*;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.IView;
import ru.khasang.cachoeira.view.mainwindow.diagram.TableAndGanttPane;
import ru.khasang.cachoeira.view.mainwindow.menubar.AbstractMenuBar;
import ru.khasang.cachoeira.view.mainwindow.properties.*;

public class MainWindowView implements IView{
    private static final Logger LOGGER = LoggerFactory.getLogger(MainWindowView.class.getName());

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

    @Override
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

        LOGGER.debug("Created.");
    }

    @Override
    public Stage getStage() {
        return stage;
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
}
