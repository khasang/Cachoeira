package ru.khasang.cachoeira.controller;

import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.Project;
import ru.khasang.cachoeira.model.Task;

import java.util.Date;

/**
 * Created by truesik on 22.10.2015.
 */
public class Controller implements IController {
    private IProject project = new Project("Test Project");
    private ITask task;

    @Override
    public IProject getDefaultProject() {
        return null;
    }

    @Override
    public IProject getProject() {
        return project;
    }

    @Override
    public void handleChangeProject(IProject project) {

    }

    @Override
    public void notifyChangeProject(IProject project) {

    }

    @Override
    public void handleAddTask(String nameOfTask, Date startDate, Date finishDate) { //todo в оригинале параметр был ITask task, но я не понял как так сделать, возможно кто-нибудь поправит
        task = new Task();
        task.setName(nameOfTask);
        task.setStartDate(startDate);
        task.setFinishDate(finishDate);
        project.getTaskList().add(task);
    }

    @Override
    public void notifyAddTask(ITask task) {

    }

    @Override
    public void handleRemoveTask(ITask task) {
        project.getTaskList().remove(task);
    }

    @Override
    public void notifyRemoveTask(ITask task) {

    }

    @Override
    public void handleChangeTask(String taskNameField, Date taskStartDate, Date taskFinishDate) { //todo тоже подправил
        task.setName(taskNameField);
        task.setStartDate(taskStartDate);
        task.setFinishDate(taskFinishDate);
    }

    @Override
    public void notifyChangeTask(ITask task) {

    }

    public ITask getSelectedTask() {
        return task;
    }

    public void setSelectedTask(ITask task) {
        this.task = task;
    }
}
