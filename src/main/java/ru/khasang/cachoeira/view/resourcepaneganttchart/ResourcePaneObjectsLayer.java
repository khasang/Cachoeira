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
 * Класс - слой на котором располагаются объекты диаграммы Ганта на вкладке Ресурсы.
 */

public class ResourcePaneObjectsLayer extends Pane {
    private UIControl uiControl;
    private List<ResourcePaneTaskBar> resourcePaneTaskBarList = new ArrayList<>();

    public ResourcePaneObjectsLayer() {
    }

    /**
     * Метод для обновления всей диаграммы.
     */
    public void refreshResourceDiagram() {
        this.getChildren().clear();
        resourcePaneTaskBarList.clear();
        for (ITask task : uiControl.getController().getProject().getTaskList()) {
            for (IResource resource : task.getResourceList()) {
                ResourcePaneTaskBar resourcePaneTaskBar = createTaskBar(uiControl, task, resource);
                this.getChildren().add(resourcePaneTaskBar);
                resourcePaneTaskBarList.add(resourcePaneTaskBar);
            }
        }
    }

    /**
     * Метод для добавления отдельно взятой метки на диаграмму.
     *
     * @param task     Задача которая присваивается к метке.
     * @param resource Ресурс который присваивается к метке.
     */
    public void addTaskBar(ITask task,
                           IResource resource) {
        ResourcePaneTaskBar resourcePaneTaskBar = createTaskBar(uiControl, task, resource);
        this.getChildren().add(resourcePaneTaskBar);
        resourcePaneTaskBarList.add(resourcePaneTaskBar);
    }

    /**
     * Метод для удаления отдельно взятой метки с диаграммы.
     *
     * @param task Задача которая присвоена к метке.
     */
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

    /**
     * Данный метод вызывается в случае удаления какого либо ресурса из таблицы.
     *
     * @param task     Задача которая присваивается к метке.
     * @param resource Ресурс который присваивается к метке.
     */
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

    /**
     * Метод для создания метки.
     *
     * @param uiControl Контроллер вью.
     * @param task      Задача которая присваивается к метке.
     * @param resource  Ресурс который присваивается к метке.
     * @return Возвращает taskBar.
     */
    private ResourcePaneTaskBar createTaskBar(UIControl uiControl,
                                              ITask task,
                                              IResource resource) {
        ResourcePaneTaskBar resourcePaneTaskBar = new ResourcePaneTaskBar();
        resourcePaneTaskBar.initTaskRectangle(uiControl, task, resource);
        resourcePaneTaskBar.setTask(task);
        resourcePaneTaskBar.setResource(resource);
        resourcePaneTaskBar.setContextMenu(uiControl.getController(), task);
        resourcePaneTaskBar.setTooltip(new TaskTooltip(task));
        return resourcePaneTaskBar;
    }

    public void setListeners(UIControl uiControl) {
        uiControl.zoomMultiplierProperty().addListener((observable -> {
            refreshResourceDiagram();
        }));
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }
}
