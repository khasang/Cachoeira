package ru.khasang.cachoeira.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.controller.IController;

import java.io.IOException;

/**
 * Created by truesik on 25.11.2015.
 */
//Здесь
public class DiagramPaneController {
    @FXML
    private Tab taskTab;
    @FXML
    private Tab resourceTab;

    private IController controller;
    private UIControl uiControl;

    public DiagramPaneController() {
//        initResourcePane();
//        initTaskPane();
    }

    public void initResourcePane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ResourcePane.fxml"));
            VBox resourcePane = loader.load();
            resourceTab.setContent(resourcePane);

            ResourcePaneController resourcePaneController = loader.getController();
            resourcePaneController.setController(controller);
            resourcePaneController.setUIControl(uiControl);
            resourcePaneController.initResourceTable();
            resourcePaneController.initContextMenus();
            resourcePaneController.initGanttChart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initTaskPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TaskPane.fxml"));
            VBox taskPane = loader.load();
            taskTab.setContent(taskPane);

            TaskPaneController taskPaneController = loader.getController();
            taskPaneController.setController(controller);
            taskPaneController.setUIControl(uiControl);
            taskPaneController.initTaskTable();
            taskPaneController.initContextMenus();
            taskPaneController.initGanttChart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setController(IController controller) {
        this.controller = controller;
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }
}
