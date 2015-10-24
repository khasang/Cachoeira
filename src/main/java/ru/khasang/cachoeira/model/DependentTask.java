package ru.khasang.cachoeira.model;

/**
 * Created by truesik on 22.10.2015.
 */
public class DependentTask implements IDependentTask {
    Task task;
    TaskDependenceType dependenceType;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public TaskDependenceType getDependence() {
        return dependenceType;
    }

    public void setDependence(TaskDependenceType dependenceType) {
        this.dependenceType = dependenceType;
    }
}
