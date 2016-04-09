package ru.khasang.cachoeira.view.mainwindow.ganttplan.objectslayer;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.scene.layout.Pane;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.objectslayer.taskbar.TaskBar;

public abstract class ObjectsLayer extends Pane {
    protected UIControl uiControl;

    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener zoomMultiplierListener;

    /**
     * Метод для добавления отдельно взятой метки на диаграмму.
     *
     * @param task Задача которая присваивается к метке.
     */
    public void addTaskBar(ITask task, IResource resource) {
        TaskBar taskBar = createTaskBar(uiControl, task, resource);
        this.getChildren().add(taskBar);
    }

    /**
     * Метод для удаления отдельно взятой метки с диаграммы.
     *
     * @param task Задача которая присвоена к метке.
     */
    public void removeTaskBar(ITask task) {
        this.getChildren().removeIf(node -> {
            TaskBar taskBar = (TaskBar) node;
            return taskBar.getTask().equals(task);
        });
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
            TaskBar taskBar = (TaskBar) node;
            return taskBar.getTask().equals(task) && taskBar.getResource().equals(resource);
        });
    }

    /**
     * Метод для поиска нужной метки по задаче.
     *
     * @param task Задача которая присвоена метке.
     * @return Возвращает метку.
     */
    public TaskBar findTaskBarByTask(ITask task) {
        return (TaskBar) this.getChildren().stream()
                .filter(node -> {
                    TaskBar taskBar = (TaskBar) node;
                    return taskBar.getTask().equals(task);
                })
                .findFirst()
                .orElse(null);
    }

    /**
     * Метод для создания метки.
     *
     * @param uiControl Контроллер вью.
     * @param task      Задача которая присваивается к метке.
     * @return Возвращает taskBar.
     */
    public abstract TaskBar createTaskBar(UIControl uiControl, ITask task, IResource resource);

    /**
     * Метод для обновления всей диаграммы.
     */
    public abstract void refreshPlan(UIControl uiControl);

    /**
     * Если переменная зума меняется то обновляем диаграмму
     *
     * @param uiControl Контроллер вью
     */
    public void setListeners(UIControl uiControl) {
        zoomMultiplierListener = observable -> refreshPlan(uiControl);
        uiControl.zoomMultiplierProperty().addListener(new WeakInvalidationListener(zoomMultiplierListener));
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }
}
