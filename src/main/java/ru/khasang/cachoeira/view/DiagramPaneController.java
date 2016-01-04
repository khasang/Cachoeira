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
public class DiagramPaneController {
    @FXML
    private Tab taskTab;
    @FXML
    private Tab resourceTab;

    private IController controller;
    private UIControl uiControl;
    private ResourcePaneController resourcePaneController;
    private TaskPaneController taskPaneController;

    public DiagramPaneController() {
    }

    /** Метод для инициализации панели с таблицей и диаграммой Ганта ресурсов**/
    public void initResourcePane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ResourcePane.fxml"));
            VBox resourcePane = loader.load();
            resourceTab.setContent(resourcePane);

            resourcePaneController = loader.getController();
            resourcePaneController.setUIControl(uiControl);
            resourcePaneController.initResourceTable();
            resourcePaneController.initContextMenus();
            resourcePaneController.initGanttChart();
            resourcePaneController.initZoom();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Метод для инициализации панели с таблицей и диаграммой Ганта задач**/
    public void initTaskPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TaskPane.fxml"));
            VBox taskPane = loader.load();
            taskTab.setContent(taskPane);

            taskPaneController = loader.getController();
            taskPaneController.setUIControl(uiControl);
            taskPaneController.initTaskTable();
            taskPaneController.initContextMenus();
            taskPaneController.initGanttChart();
            taskPaneController.initZoom();
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

    public ResourcePaneController getResourcePaneController() {
        return resourcePaneController;
    }

    public TaskPaneController getTaskPaneController() {
        return taskPaneController;
    }
}
