package ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.selectedobjectlayer.backgroundtaskbar;

import ru.khasang.cachoeira.viewcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.labelslayer.label.TaskBarLabel;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.taskbar.TaskBar;

public class TaskGanttPlanBackgroundTaskBar extends BackgroundTaskBar {
    public TaskGanttPlanBackgroundTaskBar(TaskBar taskBar, TaskBarLabel taskLabel, MainWindowController controller) {
        super(taskBar, taskLabel, controller);
    }
}
