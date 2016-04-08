package ru.khasang.cachoeira.view.ganttplan.objectslayer;

import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.ganttplan.taskbar.ResourceGanttPlanTaskBar;
import ru.khasang.cachoeira.view.ganttplan.taskbar.TaskBar;
import ru.khasang.cachoeira.view.tooltips.TaskTooltip;

public class ResourceGanttPlanObjectsLayer extends ObjectsLayer {
    @Override
    public TaskBar createTaskBar(UIControl uiControl, ITask task, IResource resource) {
        TaskBar taskBar = new ResourceGanttPlanTaskBar();
        taskBar.initTaskRectangle(uiControl, task, resource);
        taskBar.setTask(task);
        taskBar.setResource(resource);
        taskBar.setContextMenu(uiControl.getController(), task);
        taskBar.setTooltip(new TaskTooltip(task));
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
