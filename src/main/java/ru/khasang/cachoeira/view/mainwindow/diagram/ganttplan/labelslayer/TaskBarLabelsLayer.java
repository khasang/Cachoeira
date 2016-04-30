package ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.labelslayer;

import javafx.scene.layout.Pane;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.labelslayer.label.TaskBarLabel;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.taskbar.TaskBar;

public abstract class TaskBarLabelsLayer extends Pane {
    public TaskBarLabelsLayer() {
    }

    public abstract void setLabelToTaskBar(TaskBar taskBar);

    public void removeLabel(ITask task) {
        this.getChildren().removeIf(node -> {
            TaskBarLabel taskBarLabel = (TaskBarLabel) node;
            return taskBarLabel.getTaskBar().getTask().equals(task);
        });
    }

    public TaskBarLabel findLabelByTask(ITask task) {
        return (TaskBarLabel) this.getChildren().stream()
                .filter(node -> {
                    TaskBarLabel taskBarLabel = (TaskBarLabel) node;
                    return taskBarLabel.getTaskBar().getTask().equals(task);
                })
                .findFirst()
                .orElse(null);
    }
}
