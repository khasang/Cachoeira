package ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer;

import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.taskbar.ResourceGanttPlanTaskBar;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.taskbar.TaskBar;
import ru.khasang.cachoeira.viewcontroller.mainwindow.tooltipfactory.TaskTooltipFactory;
import ru.khasang.cachoeira.viewcontroller.mainwindow.tooltipfactory.TooltipFactory;

public class ResourceGanttPlanObjectsLayer extends ObjectsLayer {
    TooltipFactory<ITask> tooltipFactory = new TaskTooltipFactory();

    public ResourceGanttPlanObjectsLayer(MainWindowController controller) {
        this.controller = controller;
    }

    @Override
    public TaskBar createTaskBar(ITask task, IResource resource) {
        TaskBar taskBar = new ResourceGanttPlanTaskBar(controller);
        taskBar.initTaskRectangle(task, resource);
        taskBar.setTask(task);
        taskBar.setResource(resource);
//        taskBar.setContextMenu(uiControl.getController(), task);
        taskBar.setTooltip(tooltipFactory.createTooltip(task));
        return taskBar;
    }

    @Override
    public void refreshPlan() {
        this.getChildren().clear();
        for (ITask task : controller.getProject().getTaskList()) {
            for (IResource resource : task.getResourceList()) {
                TaskBar taskBar = createTaskBar(task, resource);
                this.getChildren().add(taskBar);
            }
        }
    }
}
