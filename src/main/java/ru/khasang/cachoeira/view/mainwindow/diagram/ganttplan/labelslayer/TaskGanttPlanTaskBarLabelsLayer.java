package ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.labelslayer;

import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.labelslayer.label.TaskBarLabel;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.labelslayer.label.TaskGanttPlanTaskBarLabel;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.taskbar.TaskBar;

public class TaskGanttPlanTaskBarLabelsLayer extends TaskBarLabelsLayer {
    @Override
    public void setLabelToTaskBar(TaskBar taskBar) {
        TaskBarLabel taskBarLabel = new TaskGanttPlanTaskBarLabel(taskBar);
        taskBarLabel.refreshResourceLabels();
        this.getChildren().add(taskBarLabel);
    }
}
