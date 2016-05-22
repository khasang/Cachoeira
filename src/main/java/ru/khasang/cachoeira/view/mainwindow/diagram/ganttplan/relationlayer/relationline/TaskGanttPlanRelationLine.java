package ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.relationlayer.relationline;

import javafx.beans.property.ObjectProperty;
import ru.khasang.cachoeira.model.TaskDependencyType;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.taskbar.TaskBar;

public class TaskGanttPlanRelationLine extends RelationLine {
    public TaskGanttPlanRelationLine(TaskBar parentTaskBar, TaskBar childTaskBar, ObjectProperty<TaskDependencyType> dependenceType) {
        super(parentTaskBar, childTaskBar, dependenceType);
    }
}
