package ru.khasang.cachoeira.view.mainwindow.ganttplan.selectedobjectlayer;

import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.labelslayer.label.TaskBarLabel;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.objectslayer.taskbar.TaskBar;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.selectedobjectlayer.backgroundtaskbar.BackgroundTaskBar;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.selectedobjectlayer.backgroundtaskbar.TaskGanttPlanBackgroundTaskBar;

public class TaskGanttPlanSelectedObjectLayer extends SelectedObjectLayer {
    @Override
    public void addBackgroundToTaskBar(TaskBar taskBar, TaskBarLabel taskPaneLabel, UIControl uiControl) {
        BackgroundTaskBar backgroundTaskBar = new TaskGanttPlanBackgroundTaskBar(
                taskBar,
                taskPaneLabel,
                uiControl
        );
        this.getChildren().add(backgroundTaskBar);
    }
}
