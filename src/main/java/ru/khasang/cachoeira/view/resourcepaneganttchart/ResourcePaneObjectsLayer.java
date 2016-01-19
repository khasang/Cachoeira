package ru.khasang.cachoeira.view.resourcepaneganttchart;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.tooltips.TaskTooltip;

/**
 * Класс - слой на котором располагаются объекты диаграммы Ганта на вкладке Ресурсы.
 */

public class ResourcePaneObjectsLayer extends Pane {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourcePaneObjectsLayer.class.getName());

    private UIControl uiControl;

    InvalidationListener zoomMultiplierListener;

    public ResourcePaneObjectsLayer() {
    }

    /**
     * Метод для обновления всей диаграммы.
     */
    public void refreshResourceDiagram() {
        this.getChildren().clear();
        for (ITask task : uiControl.getController().getProject().getTaskList()) {
            for (IResource resource : task.getResourceList()) {
                ResourcePaneTaskBar resourcePaneTaskBar = createTaskBar(uiControl, task, resource);
                this.getChildren().add(resourcePaneTaskBar);
            }
        }
        LOGGER.debug("Диаграмма ресурсов обновлена.");
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
        LOGGER.debug("Задача с именем \"{}\" добавлена к ресурсу \"{}\".", task.getName(), resource.getName());
    }

    /**
     * Метод для удаления отдельно взятой метки с диаграммы.
     *
     * @param task Задача которая присвоена к метке.
     */
    public void removeTaskBar(ITask task) {
        this.getChildren().removeIf(node -> {
            ResourcePaneTaskBar taskBar = (ResourcePaneTaskBar) node;
            return taskBar.getTask().equals(task);
        });
        LOGGER.debug("Задача с именем \"{}\" удалена.", task.getName());
//        Iterator<Node> taskBarIterator = this.getChildren().iterator();
//        while (taskBarIterator.hasNext()) {
//            ResourcePaneTaskBar resourcePaneTaskBar = (ResourcePaneTaskBar) taskBarIterator.next();
//            if (resourcePaneTaskBar.getTask().equals(task)) {
//                taskBarIterator.remove();
//                LOGGER.debug("Задача с именем \"{}\" удалена.", task.getName());
//            }
//        }
    }

    /**
     * Данный метод вызывается в случае удаления какого либо ресурса из таблицы.
     *
     * @param task     Задача которая присваивается к метке.
     * @param resource Ресурс который присваивается к метке.
     */
    public void removeTaskBarByResource(ITask task,
                                        IResource resource) {
        this.getChildren().removeIf(node -> {
            ResourcePaneTaskBar resourcePaneTaskBar = (ResourcePaneTaskBar) node;
            return resourcePaneTaskBar.getTask().equals(task) && resourcePaneTaskBar.getResource().equals(resource);
        });
        LOGGER.debug("Задача с именем \"{}\" удалена с диаграммы.", task.getName());
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

    /**
     * Если переменная зума меняется то обновляем диаграмму
     *
     * @param uiControl Контроллер вью
     */
    public void setListeners(UIControl uiControl) {
        zoomMultiplierListener = observable -> refreshResourceDiagram();
        uiControl.zoomMultiplierProperty().addListener(new WeakInvalidationListener(zoomMultiplierListener));
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }
}
