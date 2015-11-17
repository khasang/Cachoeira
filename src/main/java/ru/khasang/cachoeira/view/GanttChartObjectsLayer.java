package ru.khasang.cachoeira.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

/**
 * Created by truesik on 08.11.2015.
 */

public class GanttChartObjectsLayer extends Pane {
    private IController controller;
    private UIControl UIControl;
    private MainWindow mainWindow;
    private int columnWidth;
    private GanttChartObject ganttChartObject;

    public GanttChartObjectsLayer(IController controller, UIControl UIControl, MainWindow mainWindow, int columnWidth) {
        this.controller = controller;
        this.UIControl = UIControl;
        this.mainWindow = mainWindow;
        this.columnWidth = columnWidth;
    }

    public void refreshTaskDiagram() {
        this.getChildren().clear();

        for (ITask task : controller.getProject().getTaskList()) {
            int rowIndex = controller.getProject().getTaskList().indexOf(task);
            ganttChartObject = new GanttChartObject(controller, UIControl, mainWindow, task, rowIndex, columnWidth);
            ganttChartObject.showResourcesOnDiagram();
            this.getChildren().add(ganttChartObject);
        }
    }

    public void refreshResourceDiagram() {
        this.getChildren().clear();

        for (ITask task : controller.getProject().getTaskList()) {
            for (IResource resource : task.getResourceList()) {
                int rowIndex = controller.getProject().getResourceList().indexOf(resource);
                ganttChartObject = new GanttChartObject(controller, UIControl, mainWindow, task, rowIndex, columnWidth);
                ganttChartObject.showResourcesOnDiagram();
                this.getChildren().add(ganttChartObject);
            }
        }
    }
}
