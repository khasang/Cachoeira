package ru.khasang.cachoeira.model;

/**
 * Created by truesik on 22.10.2015.
 */
public interface IDependentTask {
    Task getTask();

    void setTask(Task task);

    TaskDependenceType getDependence();

    void setDependence(TaskDependenceType dependence);
}
