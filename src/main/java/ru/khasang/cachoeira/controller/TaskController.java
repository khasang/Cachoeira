package ru.khasang.cachoeira.controller;

import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.Task;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by truesik on 22.10.2015.
 */
public class TaskController {
    private ITask task;
    private static Set<ITask> tasks = new HashSet<>();


    public void addTask(String nameOfTask, Date startDate, Date finishDate) {
        task = new Task();
        task.setName(nameOfTask);
        task.setStartDate(startDate);
        task.setFinishDate(finishDate);
        tasks.add(task);
    }

    public static Set<ITask> getTasks() {
        return tasks;
    }

    public void removeTask(ITask task) {
        tasks.remove(task);
    }

    public void setSelectedTask(ITask task) {
        this.task = task;
    }

    public ITask getSelectedTask() {
        return task;
    }

    public void updateTask(String taskNameField, Date taskStartDate, Date taskFinishDate) {
        task.setName(taskNameField);
        task.setStartDate(taskStartDate);
        task.setFinishDate(taskFinishDate);
    }
}
