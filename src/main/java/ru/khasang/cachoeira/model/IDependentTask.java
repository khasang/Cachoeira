package ru.khasang.cachoeira.model;

/**
 * Created by truesik on 22.10.2015.
 */
public interface IDependentTask {
    ITask getTask();

    void setTask(ITask task);

    TaskDependenceType getDependenceType();

    void setDependenceType(TaskDependenceType dependenceType);
}
