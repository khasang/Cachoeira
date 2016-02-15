package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.model.IDependentTask;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.tooltips.TaskTooltip;

/**
 * Класс - слой на котором располагаются объекты диаграммы Ганта на вкладке Задачи.
 */

public class TaskPaneObjectsLayer extends Pane {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskPaneObjectsLayer.class.getName());

    private UIControl uiControl;

    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener zoomMultiplierListener;

    public TaskPaneObjectsLayer() {
    }

    /**
     * Метод для обновления всей диаграммы.
     */
    public void refreshTaskDiagram(UIControl uiControl) {
        this.getChildren().clear();
        uiControl.getController().getProject().getTaskList().forEach(task -> {
            TaskPaneTaskBar taskPaneTaskBar = createTaskBar(uiControl, task);
            this.getChildren().add(taskPaneTaskBar);
        });
        LOGGER.debug("Диаграмма задач обновлена.");
    }

    /**
     * Метод для добавления отдельно взятой метки на диаграмму.
     *
     * @param task Задача которая присваивается к метке.
     */
    public void addTaskBar(ITask task) {
        TaskPaneTaskBar taskPaneTaskBar = createTaskBar(uiControl, task);
        this.getChildren().add(taskPaneTaskBar);
        LOGGER.debug("Задача с именем \"{}\" добавлена на диаграмму.", task.getName());
    }

    /**
     * Метод для удаления отдельно взятой метки с диаграммы.
     *
     * @param task Задача которая присвоена к метке.
     */
    public void removeTaskBar(ITask task) {
        this.getChildren().removeIf(node -> {
            TaskPaneTaskBar taskBar = (TaskPaneTaskBar) node;
            return taskBar.getTask().equals(task);
        });
        LOGGER.debug("Задача с именем \"{}\" удалена с диаграммы.", task.getName());
    }

    /**
     * Метод для поиска нужной метки по задаче.
     *
     * @param task Задача которая присвоена метке.
     * @return Возвращает метку.
     */
    public TaskPaneTaskBar findTaskBarByTask(ITask task) {
        return (TaskPaneTaskBar) this.getChildren().stream()
                .filter(node -> {
                    TaskPaneTaskBar taskBar = (TaskPaneTaskBar) node;
                    return taskBar.getTask().equals(task);
                })
                .findFirst()
                .get();
    }

    public void addRelation(IDependentTask dependentTask,
                            ITask task) {
        TaskPaneTaskBar parentTaskBar = findTaskBarByTask(dependentTask.getTask());
        TaskPaneTaskBar childTaskBar = findTaskBarByTask(task);
        TaskPaneRelationLine relationLine = new TaskPaneRelationLine(parentTaskBar, childTaskBar);
        this.getChildren().add(relationLine);
    }

    /**
     * Метод для создания метки.
     *
     * @param uiControl Контроллер вью.
     * @param task      Задача которая присваивается к метке.
     * @return Возвращает taskBar.
     */
    private TaskPaneTaskBar createTaskBar(UIControl uiControl,
                                          ITask task) {
        TaskPaneTaskBar taskPaneTaskBar = new TaskPaneTaskBar();
        taskPaneTaskBar.initTaskRectangle(uiControl, task);
        taskPaneTaskBar.setTask(task);
        taskPaneTaskBar.setContextMenu(uiControl.getController(), task);
        taskPaneTaskBar.setTooltip(new TaskTooltip(task));
        return taskPaneTaskBar;
    }

    /**
     * Если переменная зума меняется то обновляем диаграмму
     *
     * @param uiControl Контроллер вью
     */
    public void setListeners(UIControl uiControl) {
        zoomMultiplierListener = observable -> refreshTaskDiagram(uiControl);
        uiControl.zoomMultiplierProperty().addListener(new WeakInvalidationListener(zoomMultiplierListener));
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }
}
