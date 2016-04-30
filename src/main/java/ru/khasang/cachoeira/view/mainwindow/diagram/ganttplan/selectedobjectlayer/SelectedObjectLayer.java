package ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.selectedobjectlayer;

import javafx.scene.layout.Pane;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.labelslayer.label.TaskBarLabel;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.taskbar.TaskBar;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.selectedobjectlayer.backgroundtaskbar.BackgroundTaskBar;
import ru.khasang.cachoeira.viewcontroller.UIControl;

public abstract class SelectedObjectLayer extends Pane {
    public SelectedObjectLayer() {
    }

    public abstract void addBackgroundToTaskBar(TaskBar taskBar,
                                                TaskBarLabel taskPaneLabel,
                                                MainWindowController controller);

    public void removeBackgroundTaskBar(ITask task) {
        this.getChildren().removeIf(node -> {
            BackgroundTaskBar backgroundTaskBar = (BackgroundTaskBar) node;
            return backgroundTaskBar.getTaskBar().getTask().equals(task);
        });
    }
}
