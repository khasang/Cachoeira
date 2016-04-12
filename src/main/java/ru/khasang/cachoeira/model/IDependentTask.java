package ru.khasang.cachoeira.model;

import javafx.beans.property.ObjectProperty;

public interface IDependentTask {
    ITask getTask();

    ObjectProperty<ITask> taskProperty();

    void setTask(ITask task);

    TaskDependencyType getDependenceType();

    ObjectProperty<TaskDependencyType> dependenceTypeProperty();

    void setDependenceType(TaskDependencyType dependenceType);
}
