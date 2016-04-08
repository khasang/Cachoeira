package ru.khasang.cachoeira.view.mainwindow.ganttplan.objectslayer;

import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.labelslayer.label.TaskBarLabel;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.objectslayer.taskbar.TaskBar;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.objectslayer.taskbar.TaskGanttPlanTaskBar;
import ru.khasang.cachoeira.view.mainwindow.tooltips.TaskTooltip;

public class TaskGanttPlanObjectsLayer extends ObjectsLayer {
    @Override
    public void removeTaskBar(ITask task) {
        super.removeTaskBar(task);
        uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController().getGanttPlan().getLabelsLayer().removeLabel(task);
    }

    @Override
    public TaskBar createTaskBar(UIControl uiControl, ITask task, IResource resource) {
        TaskBar taskBar = new TaskGanttPlanTaskBar();
        taskBar.initTaskRectangle(uiControl, task, null);
        taskBar.setTask(task);
        taskBar.setContextMenu(uiControl.getController(), task);
        taskBar.setTooltip(new TaskTooltip(task));
//         Создаем "панель" на которой будут отображаться привязанные ресурсы
        uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController()
                .getGanttPlan().getLabelsLayer()
                .setLabelToTaskBar(taskBar);
        // Создаем "панель" которая меняет, цвет если эта задача выделена
        TaskBarLabel paneLabel = uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController()
                .getGanttPlan().getLabelsLayer()
                .findLabelByTask(task);
        uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController()
                .getGanttPlan().getSelectedObjectLayer()
                .addBackgroundToTaskBar(taskBar, paneLabel, uiControl);
        return taskBar;
    }

    @Override
    public void refreshPlan(UIControl uiControl) {
        this.getChildren().clear();
        uiControl.getController().getProject().getTaskList().forEach(task -> {
            TaskBar taskPaneTaskBar = createTaskBar(uiControl, task, null);
            this.getChildren().add(taskPaneTaskBar);
        });
    }
}
