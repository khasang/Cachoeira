package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.relationline;

import javafx.beans.property.ObjectProperty;
import ru.khasang.cachoeira.model.TaskDependencyType;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.TaskBar;

/**
 * RelationLine implementation.
 */
public class TaskGanttChartRelationLine extends RelationLine {
    /**
     * @param parentTaskBar
     * @param childTaskBar
     * @param dependenceType
     */
    public TaskGanttChartRelationLine(TaskBar parentTaskBar,
                                      TaskBar childTaskBar,
                                      ObjectProperty<TaskDependencyType> dependenceType) {
        super(parentTaskBar, childTaskBar, dependenceType);
    }
}
