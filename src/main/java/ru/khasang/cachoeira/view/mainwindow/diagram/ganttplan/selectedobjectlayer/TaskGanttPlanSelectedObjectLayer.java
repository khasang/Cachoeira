package ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.selectedobjectlayer;

import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.labelslayer.label.TaskBarLabel;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.taskbar.TaskBar;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.selectedobjectlayer.backgroundtaskbar.BackgroundTaskBar;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.selectedobjectlayer.backgroundtaskbar.TaskGanttPlanBackgroundTaskBar;

public class TaskGanttPlanSelectedObjectLayer extends SelectedObjectLayer {
    @Override
    public void addBackgroundToTaskBar(TaskBar taskBar, TaskBarLabel taskPaneLabel, MainWindowController controller) {
        BackgroundTaskBar backgroundTaskBar = new TaskGanttPlanBackgroundTaskBar(
                taskBar,
                taskPaneLabel,
                controller
        );
        this.getChildren().add(backgroundTaskBar);
    }
}
