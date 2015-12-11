package ru.khasang.cachoeira.view.resourcepaneganttchart;

import javafx.scene.layout.Pane;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by truesik on 08.11.2015.
 */

public class ResourcePaneObjectsLayer extends Pane {
    private IController controller;
    private int columnWidth;
    private ResourcePaneTaskBar resourcePaneTaskBar;
    private UIControl uiControl;
    private List<ResourcePaneTaskBar> resourcePaneTaskBarList = new ArrayList<>();

    public ResourcePaneObjectsLayer(IController controller, int columnWidth) {
        this.controller = controller;
        this.columnWidth = columnWidth;
    }

    public void addTaskBar(ITask task, IResource resource) {
        resourcePaneTaskBar = new ResourcePaneTaskBar(controller, task, columnWidth, uiControl, resource);
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

    public void removeTaskBarByResource(ITask task, IResource resource) {
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

    public void refreshResourceDiagram() {
        this.getChildren().clear();

        for (ITask task : controller.getProject().getTaskList()) {
            for (IResource resource : task.getResourceList()) {
                resourcePaneTaskBar = new ResourcePaneTaskBar(controller, task, columnWidth, uiControl, resource);
                this.getChildren().add(resourcePaneTaskBar);
            }
        }
    }
}
