package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.scene.layout.Pane;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by truesik on 08.11.2015.
 */

public class TaskPaneObjectsLayer extends Pane {
    private IController controller;
    private int columnWidth;
    private TaskPaneTaskBar taskPaneTaskBar;
    private UIControl uiControl;
    private List<TaskPaneTaskBar> taskPaneTaskBarList = new ArrayList<>();

    public TaskPaneObjectsLayer(IController controller, int columnWidth) {
        this.controller = controller;
        this.columnWidth = columnWidth;
    }

    public void refreshTaskDiagram() {
        this.getChildren().clear();
        for (ITask task : controller.getProject().getTaskList()) {
            taskPaneTaskBar = new TaskPaneTaskBar(controller, task, columnWidth, uiControl);
//            taskPaneTaskBar.showResourcesOnDiagram();
            this.getChildren().add(taskPaneTaskBar);
        }
    }

    public void addTaskBar(ITask task) {
        taskPaneTaskBar = new TaskPaneTaskBar(controller, task, columnWidth, uiControl);
        this.getChildren().add(taskPaneTaskBar);
        taskPaneTaskBarList.add(taskPaneTaskBar);
    }

    public void removeTaskBar(ITask task) {
        for (TaskPaneTaskBar taskPaneTaskBar : taskPaneTaskBarList) {
            if (taskPaneTaskBar.getTask().equals(task)) {
                this.getChildren().remove(taskPaneTaskBar);
                taskPaneTaskBarList.remove(taskPaneTaskBar);
                break;
            }
        }
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }
}
