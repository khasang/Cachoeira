package ru.khasang.cachoeira.view.ganttchart;

import javafx.scene.layout.Pane;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by truesik on 08.11.2015.
 */

public class ObjectsLayer extends Pane {
    private IController controller;
    private int columnWidth;
    private ChartObject chartObject;
    private List<ChartObject> chartObjectList = new ArrayList<>();

    public ObjectsLayer(IController controller, int columnWidth) {
        this.controller = controller;
        this.columnWidth = columnWidth;
    }

    public void refreshTaskDiagram() {
        this.getChildren().clear();

        for (ITask task : controller.getProject().getTaskList()) {
            int rowIndex = controller.getProject().getTaskList().indexOf(task);
            chartObject = new ChartObject(controller, task, rowIndex, columnWidth);
//            chartObject.showResourcesOnDiagram();
            this.getChildren().add(chartObject);
        }
    }

    public void refreshResourceDiagram() {
        this.getChildren().clear();

        for (ITask task : controller.getProject().getTaskList()) {
            for (IResource resource : task.getResourceList()) {
                int rowIndex = controller.getProject().getResourceList().indexOf(resource);
                chartObject = new ChartObject(controller, task, rowIndex, columnWidth);
//                chartObject.showResourcesOnDiagram();
                this.getChildren().add(chartObject);
            }
        }
    }

    public void addTaskBar(ITask task) {
        int rowIndex = controller.getProject().getTaskList().indexOf(task);
        chartObject = new ChartObject(controller, task, rowIndex, columnWidth);
        this.getChildren().add(chartObject);
        chartObjectList.add(chartObject);
    }

    public void removeTaskBar(ITask task) {
        for (ChartObject taskBar : chartObjectList) {
            if (taskBar.getTask().equals(task)) {
                this.getChildren().remove(taskBar);
                chartObjectList.remove(taskBar);
                break;
            }
        }
    }
}
