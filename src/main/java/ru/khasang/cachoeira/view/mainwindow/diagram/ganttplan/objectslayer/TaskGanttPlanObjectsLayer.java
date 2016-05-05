package ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer;

import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.taskbar.TaskBar;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.taskbar.TaskGanttPlanTaskBar;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.labelslayer.label.TaskBarLabel;
import ru.khasang.cachoeira.viewcontroller.mainwindow.tooltipfactory.TaskTooltipFactory;
import ru.khasang.cachoeira.viewcontroller.mainwindow.tooltipfactory.TooltipFactory;

public class TaskGanttPlanObjectsLayer extends ObjectsLayer {
    TooltipFactory<ITask> tooltipFactory = new TaskTooltipFactory();

    public TaskGanttPlanObjectsLayer(MainWindowController controller) {
        this.controller = controller;
    }

    @Override
    public void removeTaskBar(ITask task) {
        super.removeTaskBar(task);
        controller.getView().getTaskGanttPlan().getLabelsLayer().removeLabel(task);
    }

    @Override
    public TaskBar createTaskBar(ITask task, IResource resource) {
        TaskBar taskBar = new TaskGanttPlanTaskBar(controller);
        taskBar.initTaskRectangle(task, null);
        taskBar.setTask(task);
//        taskBar.setContextMenu(uiControl.getController(), task);
        taskBar.setTooltip(tooltipFactory.createTooltip(task));
//         Создаем "панель" на которой будут отображаться привязанные ресурсы
        controller.getView().getTaskGanttPlan().getLabelsLayer().setLabelToTaskBar(taskBar);
        // Создаем "панель" которая меняет, цвет если эта задача выделена
        TaskBarLabel paneLabel = controller.getView().getTaskGanttPlan().getLabelsLayer().findLabelByTask(task);
        controller.getView().getTaskGanttPlan().getSelectedObjectLayer().addBackgroundToTaskBar(taskBar, paneLabel, controller);
        return taskBar;
    }

    @Override
    public void refreshPlan() {
        this.getChildren().clear();
        controller.getProject().getTaskList().forEach(task -> {
            TaskBar taskPaneTaskBar = createTaskBar(task, null);
            this.getChildren().add(taskPaneTaskBar);
        });
    }
}