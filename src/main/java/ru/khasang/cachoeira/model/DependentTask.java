package ru.khasang.cachoeira.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class DependentTask implements IDependentTask {
    private ObjectProperty<ITask> task = new SimpleObjectProperty<>(this, "task");
    private ObjectProperty<TaskDependencyType> dependenceType = new SimpleObjectProperty<>(this, "dependenceType");

    public DependentTask() {
    }

    public DependentTask(ITask task, TaskDependencyType dependenceType) {
        this.task.setValue(task);
        this.dependenceType.setValue(dependenceType);
    }

    @Override
    public ITask getTask() {
        return task.get();
    }

    @Override
    public ObjectProperty<ITask> taskProperty() {
        return task;
    }

    @Override
    public void setTask(ITask task) {
        this.task.set(task);
    }

    @Override
    public TaskDependencyType getDependenceType() {
        return dependenceType.get();
    }

    @Override
    public ObjectProperty<TaskDependencyType> dependenceTypeProperty() {
        return dependenceType;
    }

    @Override
    public void setDependenceType(TaskDependencyType dependenceType) {
        this.dependenceType.set(dependenceType);
    }
}
