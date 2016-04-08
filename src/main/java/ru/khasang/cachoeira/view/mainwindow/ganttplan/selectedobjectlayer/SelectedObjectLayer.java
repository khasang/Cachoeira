package ru.khasang.cachoeira.view.mainwindow.ganttplan.selectedobjectlayer;

import javafx.scene.layout.Pane;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.labelslayer.label.TaskBarLabel;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.objectslayer.taskbar.TaskBar;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.selectedobjectlayer.backgroundtaskbar.BackgroundTaskBar;

public abstract class SelectedObjectLayer extends Pane {
    public SelectedObjectLayer() {
    }

    public abstract void addBackgroundToTaskBar(TaskBar taskBar,
                                                TaskBarLabel taskPaneLabel,
                                                UIControl uiControl);

    public void removeBackgroundTaskBar(ITask task) {
        this.getChildren().removeIf(node -> {
            BackgroundTaskBar backgroundTaskBar = (BackgroundTaskBar) node;
            return backgroundTaskBar.getTaskBar().getTask().equals(task);
        });
    }
}
