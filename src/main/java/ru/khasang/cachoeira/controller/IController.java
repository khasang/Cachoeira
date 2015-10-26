package ru.khasang.cachoeira.controller;

import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.ITask;

import java.util.Date;

/**
 * Created by truesik on 26.10.2015.
 */
public interface IController {
    IProject getDefaultProject();

    IProject getProject();

    void handleChangeProject(IProject project);

    void notifyChangeProject(IProject project);

    void handleAddTask(String nameOfTask, Date startDate, Date finishDate);

    void notifyAddTask(ITask task);

    void handleRemoveTask(ITask task);

    void notifyRemoveTask(ITask task);

    void handleChangeTask(String taskNameField, Date taskStartDate, Date taskFinishDate);

    void notifyChangeTask(ITask task);

    //мои добавки
    ITask getSelectedTask();

    void setSelectedTask(ITask task);
}
