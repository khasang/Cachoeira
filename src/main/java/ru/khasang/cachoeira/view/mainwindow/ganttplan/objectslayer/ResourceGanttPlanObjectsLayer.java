package ru.khasang.cachoeira.view.mainwindow.ganttplan.objectslayer;

import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.objectslayer.taskbar.ResourceGanttPlanTaskBar;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.objectslayer.taskbar.TaskBar;
import ru.khasang.cachoeira.view.mainwindow.tooltips.TaskTooltipFactory;
import ru.khasang.cachoeira.view.mainwindow.tooltips.TooltipFactory;

public class ResourceGanttPlanObjectsLayer extends ObjectsLayer {
    TooltipFactory<ITask> tooltipFactory = new TaskTooltipFactory();

    @Override
    public TaskBar createTaskBar(UIControl uiControl, ITask task, IResource resource) {
        TaskBar taskBar = new ResourceGanttPlanTaskBar();
        taskBar.initTaskRectangle(uiControl, task, resource);
        taskBar.setTask(task);
        taskBar.setResource(resource);
        taskBar.setContextMenu(uiControl.getController(), task);
        taskBar.setTooltip(tooltipFactory.createTooltip(task));
        return taskBar;
    }

    @Override
    public void refreshPlan(UIControl uiControl) {
        this.getChildren().clear();
        for (ITask task : uiControl.getController().getProject().getTaskList()) {
            for (IResource resource : task.getResourceList()) {
                TaskBar taskBar = createTaskBar(uiControl, task, resource);
                this.getChildren().add(taskBar);
            }
        }
    }
}
