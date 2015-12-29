package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.scene.layout.Pane;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.tooltips.TaskTooltip;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс - слой на котором располагаются метки задач.
 */

public class TaskPaneObjectsLayer extends Pane {
    private UIControl uiControl;
    private List<TaskPaneTaskBar> taskPaneTaskBarList = new ArrayList<>();

    public TaskPaneObjectsLayer() {
    }

    /**
     * Метод для обновления всей диаграммы.
     */
    public void refreshTaskDiagram() {
        this.getChildren().clear();
        taskPaneTaskBarList.clear();
        for (ITask task : uiControl.getController().getProject().getTaskList()) {
            TaskPaneTaskBar taskPaneTaskBar = createTaskBar(uiControl, task);
            this.getChildren().add(taskPaneTaskBar);
            taskPaneTaskBarList.add(taskPaneTaskBar);
        }
    }

    /**
     * Метод для добавления отдельно взятой метки на диаграмму.
     *
     * @param task Задача которая присваивается к метке.
     */
    public void addTaskBar(ITask task) {
        TaskPaneTaskBar taskPaneTaskBar = createTaskBar(uiControl, task);
        this.getChildren().add(taskPaneTaskBar);
        taskPaneTaskBarList.add(taskPaneTaskBar);
    }

    /**
     * Метод для удаления отдельно взятой метки с диаграммы.
     *
     * @param task Задача которая присвоена к метке.
     */
    public void removeTaskBar(ITask task) {
        for (TaskPaneTaskBar taskPaneTaskBar : taskPaneTaskBarList) {
            if (taskPaneTaskBar.getTask().equals(task)) {
                this.getChildren().remove(taskPaneTaskBar);
                taskPaneTaskBarList.remove(taskPaneTaskBar);
                break;
            }
        }
    }

    /**
     * Метод для создания метки.
     *
     * @param uiControl Контроллер вью.
     * @param task      Задача которая присваивается к метке.
     * @return Возвращает taskBar.
     */
    private TaskPaneTaskBar createTaskBar(UIControl uiControl, ITask task) {
        TaskPaneTaskBar taskPaneTaskBar = new TaskPaneTaskBar();
        taskPaneTaskBar.initTaskRectangle(uiControl, task);
        taskPaneTaskBar.setTask(task);
        taskPaneTaskBar.setContextMenu(uiControl.getController(), task);
        taskPaneTaskBar.setTooltip(new TaskTooltip(task));
        return taskPaneTaskBar;
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }
}
