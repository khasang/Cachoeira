package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.scene.layout.Pane;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;

public class TaskPaneSelectedObjectLayer extends Pane {
    public TaskPaneSelectedObjectLayer() {
    }

    public void addBackgroundToTaskBar(TaskPaneTaskBar taskBar,
                                       TaskPaneLabel taskPaneLabel,
                                       UIControl uiControl) {
        TaskPaneBackgroundTaskBar backgroundTaskBar = new TaskPaneBackgroundTaskBar(
                taskBar,
                taskPaneLabel,
                uiControl
        );
        this.getChildren().add(backgroundTaskBar);
    }

    public void removeBackgroundTaskBar(ITask task) {
        this.getChildren().removeIf(node -> {
            TaskPaneBackgroundTaskBar backgroundTaskBar = (TaskPaneBackgroundTaskBar) node;
            return backgroundTaskBar.getTaskBar().getTask().equals(task);
        });
    }
}
