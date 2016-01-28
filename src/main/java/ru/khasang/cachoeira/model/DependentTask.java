package ru.khasang.cachoeira.model;

/**
 * Created by truesik on 22.10.2015.
 */
public class DependentTask implements IDependentTask {
    private ITask task;
    private TaskDependenceType dependenceType;

    public ITask getTask() {
        return task;
    }

    public void setTask(ITask task) {
        this.task = task;
    }

    public TaskDependenceType getDependenceType() {
        return dependenceType;
    }

    public void setDependenceType(TaskDependenceType dependenceType) {
        this.dependenceType = dependenceType;
    }
}
