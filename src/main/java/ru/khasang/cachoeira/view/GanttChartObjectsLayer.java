package ru.khasang.cachoeira.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import ru.khasang.cachoeira.controller.IController;
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

    public GanttChartObjectsLayer(IController controller, UIControl UIControl, MainWindow mainWindow, int columnWidth) {
        this.controller = controller;
        this.UIControl = UIControl;
        this.mainWindow = mainWindow;
        this.columnWidth = columnWidth;
    }

    public void refreshDiagram() {
        this.getChildren().clear();
        taskTableModel.clear();
        taskTableModel.addAll(controller.getProject().getTaskList());

        for (ITask task : taskTableModel) {
            int rowIndex = taskTableModel.indexOf(task);
            GanttChartObject ganttChartObject = new GanttChartObject(controller, UIControl, mainWindow, task, rowIndex, columnWidth);
            this.getChildren().add(ganttChartObject);
        }
    }
}
