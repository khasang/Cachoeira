package ru.khasang.cachoeira.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private ObservableList<ITask> taskTableModel = FXCollections.observableArrayList();
    private ObservableList<IResource> resourceTableModel = FXCollections.observableArrayList();

    public GanttChartObjectsLayer(IController controller, UIControl UIControl, MainWindow mainWindow, int columnWidth) {
        this.controller = controller;
        this.UIControl = UIControl;
        this.mainWindow = mainWindow;
        this.columnWidth = columnWidth;
    }

    public void refreshTaskDiagram() {
        this.getChildren().clear();
        taskTableModel.clear();
        taskTableModel.addAll(controller.getProject().getTaskList());

        for (ITask task : taskTableModel) {
            int rowIndex = taskTableModel.indexOf(task);
            GanttChartObject ganttChartObject = new GanttChartObject(controller, UIControl, mainWindow, task, rowIndex, columnWidth);
            this.getChildren().add(ganttChartObject);
        }
    }

    public void refreshResourceDiagram() {
        this.getChildren().clear();
        taskTableModel.clear();
        resourceTableModel.clear();
        taskTableModel.addAll(controller.getProject().getTaskList());
        resourceTableModel.addAll(controller.getProject().getResourceList());

        for (ITask task : taskTableModel) {
            for (IResource resource : task.getResourceList()) {
                int rowIndex = resourceTableModel.indexOf(resource);
                GanttChartObject ganttChartObject = new GanttChartObject(controller, UIControl, mainWindow, task, rowIndex, columnWidth);
                this.getChildren().add(ganttChartObject);
            }
        }
    }
}
