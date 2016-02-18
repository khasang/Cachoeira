package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.scene.layout.Pane;

/**
 * Created by Вячеслав on 17.02.2016.
 */
public class TaskPaneLabelLayer extends Pane {
    public TaskPaneLabelLayer() {
    }

    public void setLabelToTaskBar(TaskPaneTaskBar taskBar) {
        TaskPaneLabel taskPaneLabel = new TaskPaneLabel(taskBar);
        this.getChildren().add(taskPaneLabel);
    }

    public void removeLabel(TaskPaneTaskBar taskBar) {

    }
}
