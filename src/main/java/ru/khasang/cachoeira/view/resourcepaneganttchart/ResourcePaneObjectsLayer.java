package ru.khasang.cachoeira.view.resourcepaneganttchart;

import javafx.scene.layout.Pane;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.tooltips.TaskTooltip;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by truesik on 08.11.2015.
 */

public class ResourcePaneObjectsLayer extends Pane {
    private int columnWidth;
    private ResourcePaneTaskBar resourcePaneTaskBar;
    private UIControl uiControl;
    private List<ResourcePaneTaskBar> resourcePaneTaskBarList = new ArrayList<>();

    public ResourcePaneObjectsLayer(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public void refreshResourceDiagram() {
        this.getChildren().clear();
        for (ITask task : uiControl.getController().getProject().getTaskList()) {
            for (IResource resource : task.getResourceList()) {
                resourcePaneTaskBar = new ResourcePaneTaskBar(columnWidth);
                resourcePaneTaskBar.createTaskRectangle(uiControl, task, resource);
                resourcePaneTaskBar.setTask(task);
                resourcePaneTaskBar.setResource(resource);
                resourcePaneTaskBar.enableDrag(uiControl, task);
                resourcePaneTaskBar.setContextMenu(uiControl, task);
                resourcePaneTaskBar.setTooltip(new TaskTooltip(task));
                this.getChildren().add(resourcePaneTaskBar);
            }
        }
    }

    public void addTaskBar(ITask task,
                           IResource resource) {
        resourcePaneTaskBar = new ResourcePaneTaskBar(columnWidth);
        resourcePaneTaskBar.createTaskRectangle(uiControl, task, resource);
        resourcePaneTaskBar.setTask(task);
        resourcePaneTaskBar.setResource(resource);
        resourcePaneTaskBar.enableDrag(uiControl, task);
        resourcePaneTaskBar.setContextMenu(uiControl, task);
        resourcePaneTaskBar.setTooltip(new TaskTooltip(task));
        this.getChildren().add(resourcePaneTaskBar);
        resourcePaneTaskBarList.add(resourcePaneTaskBar);
    }

    public void removeTaskBar(ITask task) {
        Iterator<ResourcePaneTaskBar> taskBarIterator = resourcePaneTaskBarList.iterator();
        while (taskBarIterator.hasNext()) {
            ResourcePaneTaskBar resourcePaneTaskBar = taskBarIterator.next();
            if (resourcePaneTaskBar.getTask().equals(task)) {
                this.getChildren().remove(resourcePaneTaskBar);
                taskBarIterator.remove();
            }
        }
    }

    public void removeTaskBarByResource(ITask task,
                                        IResource resource) {
        Iterator<ResourcePaneTaskBar> taskBarIterator = resourcePaneTaskBarList.iterator();
        while (taskBarIterator.hasNext()) {
            ResourcePaneTaskBar resourcePaneTaskBar = taskBarIterator.next();
            if (resourcePaneTaskBar.getTask().equals(task) && resourcePaneTaskBar.getResource().equals(resource)) {
                this.getChildren().remove(resourcePaneTaskBar);
                taskBarIterator.remove();
            }
        }
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }
}
