package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.scene.layout.Pane;
import ru.khasang.cachoeira.model.ITask;

public class TaskPaneLabelLayer extends Pane {
    public TaskPaneLabelLayer() {
    }

    public void setLabelToTaskBar(TaskPaneTaskBar taskBar) {
        TaskPaneLabel taskPaneLabel = new TaskPaneLabel(taskBar);
        this.getChildren().add(taskPaneLabel);
    }

    public void removeLabel(ITask task) {
        this.getChildren().removeIf(node -> {
            TaskPaneLabel taskPaneLabel = (TaskPaneLabel) node;
            return taskPaneLabel.getTaskBar().getTask().equals(task);
        });
    }
}
