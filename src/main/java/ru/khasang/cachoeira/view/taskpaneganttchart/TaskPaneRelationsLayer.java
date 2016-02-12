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
 * Created by Вячеслав on 12.02.2016.
 */
public class TaskPaneRelationsLayer extends Pane {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskPaneRelationsLayer.class.getName());

    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener zoomMultiplierListener;

    public TaskPaneRelationsLayer() {
    }

    public void refreshRelationsDiagram(UIControl uiControl) {
        this.getChildren().clear();
        uiControl.getController().getProject().getTaskList().stream()
                .flatMap(task -> task.getParentTasks().stream())
                .map(dependentTask -> dependentTask.getTask())
                .forEach();
        uiControl.getController().getProject().getTaskList().forEach(task -> {
            TaskPaneTaskBar taskPaneTaskBar = createTaskBar(uiControl, task);
            this.getChildren().add(taskPaneTaskBar);
        });
        LOGGER.debug("Диаграмма связей обновлена.");
    }

    public void addBackgroundTaskBar(UIControl uiControl) {

    }

    public void addRelation(IDependentTask dependentTask, UIControl uiControl) {
        TaskPaneRelationLine taskPaneRelationLine = new TaskPaneRelationLine();
        this.getChildren().add(taskPaneRelationLine);
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
        zoomMultiplierListener = observable -> refreshRelationsDiagram(uiControl);
        uiControl.zoomMultiplierProperty().addListener(new WeakInvalidationListener(zoomMultiplierListener));
    }
}
