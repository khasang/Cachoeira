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
    private double rowHeight = 24;
    private IController controller;
    private UIControl UIControl;
    private MainWindow mainWindow;
    private ObservableList<ITask> taskTableModel = FXCollections.observableArrayList();

    public GanttChartObjectsLayer(IController controller, UIControl UIControl, MainWindow mainWindow) {
        this.controller = controller;
        this.UIControl = UIControl;
        this.mainWindow = mainWindow;
    }

    public void refreshDiagram() {
        this.getChildren().clear();
        taskTableModel.clear();
        taskTableModel.addAll(controller.getProject().getTaskList());

        double rowIndex = 0;
        for (ITask task : taskTableModel) {
            GanttChartObject ganttChartObject = new GanttChartObject(controller, UIControl, mainWindow, task, rowIndex);
            this.getChildren().add(ganttChartObject);
            rowIndex = rowIndex + rowHeight;
        }
    }


}
