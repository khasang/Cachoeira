package ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer;

import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.vcontroller.tooltipfactory.TaskTooltipFactory;
import ru.khasang.cachoeira.vcontroller.tooltipfactory.TooltipFactory;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.taskbar.TaskBar;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.taskbar.TaskGanttPlanTaskBar;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.labelslayer.label.TaskBarLabel;

public class TaskGanttPlanObjectsLayer extends ObjectsLayer {
    TooltipFactory<ITask> tooltipFactory = new TaskTooltipFactory();

    public TaskGanttPlanObjectsLayer(MainWindowController controller) {
        this.controller = controller;
    }

    @Override
    public void removeTaskBar(ITask task) {
        super.removeTaskBar(task);
        controller.getTaskGanttPlan().getLabelsLayer().removeLabel(task);
    }

    @Override
    public TaskBar createTaskBar(ITask task, IResource resource) {
        TaskBar taskBar = new TaskGanttPlanTaskBar(controller);
        taskBar.initTaskRectangle(task, null);
        taskBar.setTask(task);
        taskBar.setContextMenu(task);
        taskBar.setTooltip(tooltipFactory.createTooltip(task));
//         Создаем "панель" на которой будут отображаться привязанные ресурсы
        controller.getTaskGanttPlan().getLabelsLayer().setLabelToTaskBar(taskBar);
        // Создаем "панель" которая меняет, цвет если эта задача выделена
        TaskBarLabel paneLabel = controller.getTaskGanttPlan().getLabelsLayer().findLabelByTask(task);
        controller.getTaskGanttPlan().getSelectedObjectLayer().addBackgroundToTaskBar(taskBar, paneLabel, controller);
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
