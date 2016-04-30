package ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.selectedobjectlayer.backgroundtaskbar;

import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.labelslayer.label.TaskBarLabel;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.taskbar.TaskBar;
import ru.khasang.cachoeira.viewcontroller.UIControl;

public class TaskGanttPlanBackgroundTaskBar extends BackgroundTaskBar {
    public TaskGanttPlanBackgroundTaskBar(TaskBar taskBar, TaskBarLabel taskLabel, MainWindowController controller) {
        super(taskBar, taskLabel, controller);
    }
}
