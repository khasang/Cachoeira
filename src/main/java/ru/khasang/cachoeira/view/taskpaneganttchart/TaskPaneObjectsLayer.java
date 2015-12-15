package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.scene.layout.Pane;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.tooltips.TaskTooltip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by truesik on 08.11.2015.
 */

public class TaskPaneObjectsLayer extends Pane {
    private int columnWidth;
    private TaskPaneTaskBar taskPaneTaskBar;
    private UIControl uiControl;
    private List<TaskPaneTaskBar> taskPaneTaskBarList = new ArrayList<>();

    public TaskPaneObjectsLayer(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public void refreshTaskDiagram() {
        this.getChildren().clear();
        for (ITask task : uiControl.getController().getProject().getTaskList()) {
            taskPaneTaskBar = new TaskPaneTaskBar(columnWidth);
            taskPaneTaskBar.createTaskRectangle(uiControl, task);
            taskPaneTaskBar.setTask(task);
            taskPaneTaskBar.enableDrag(uiControl, task);
            taskPaneTaskBar.setContextMenu(uiControl, task);
            taskPaneTaskBar.setTooltip(new TaskTooltip(task));
            this.getChildren().add(taskPaneTaskBar);
        }
    }

    public void addTaskBar(ITask task) {
        taskPaneTaskBar = new TaskPaneTaskBar(columnWidth);
        taskPaneTaskBar.createTaskRectangle(uiControl, task);
        taskPaneTaskBar.setTask(task);
        taskPaneTaskBar.enableDrag(uiControl, task);
        taskPaneTaskBar.setContextMenu(uiControl, task);
        taskPaneTaskBar.setTooltip(new TaskTooltip(task));
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
