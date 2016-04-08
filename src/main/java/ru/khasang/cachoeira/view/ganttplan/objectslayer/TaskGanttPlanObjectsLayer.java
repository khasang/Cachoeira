package ru.khasang.cachoeira.view.ganttplan.objectslayer;

import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.ganttplan.taskbar.TaskBar;
import ru.khasang.cachoeira.view.ganttplan.taskbar.TaskGanttPlanTaskBar;
import ru.khasang.cachoeira.view.tooltips.TaskTooltip;

/**
 * Created by Вячеслав on 07.04.2016.
 */
public class TaskGanttPlanObjectsLayer extends ObjectsLayer {
    @Override
    public TaskBar createTaskBar(UIControl uiControl, ITask task, IResource resource) {
        TaskBar taskBar = new TaskGanttPlanTaskBar();
        taskBar.initTaskRectangle(uiControl, task, null);
        taskBar.setTask(task);
        taskBar.setContextMenu(uiControl.getController(), task);
        taskBar.setTooltip(new TaskTooltip(task));
        // Создаем "панель" на которой будут отображаться привязанные ресурсы
//        uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController()
//                .getGanttPlan().getTaskPaneLabelLayer()
//                .setLabelToTaskBar(taskBar);
        // Создаем "панель" которая меняет, цвет если эта задача выделена
//        TaskPaneLabel paneLabel = uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController()
//                .getGanttPlan().getTaskPaneLabelLayer()
//                .findLabelByTask(task);
//        uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController()
//                .getGanttPlan().getTaskPaneSelectedObjectLayer()
//                .addBackgroundToTaskBar(taskPaneTaskBar, paneLabel, uiControl);
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
