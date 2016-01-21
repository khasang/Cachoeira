package ru.khasang.cachoeira.controller;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import ru.khasang.cachoeira.model.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by truesik on 26.10.2015.
 */
public interface IController {
    IProject getDefaultProject();

    IProject getProject();

    void handleChangeProject(IProject project);

    void handleAddTask(ITask task);

    void handleRemoveTask(ITask task);

    void handleChangeTask(ITask task,
                          String taskNameField,
                          LocalDate taskStartDate,
                          LocalDate taskFinishDate,
                          int duration,
                          Double taskCost,
                          double taskDonePercent,
                          PriorityType taskPriorityType,
                          ObservableList<IResource> resources);

    ITask getSelectedTask();

    void setSelectedTask(ITask task);

    ObjectProperty<ITask> selectedTaskProperty();

    void handleAddResource(IResource resource);

    void handleRemoveResource(IResource resource);

    void handleChangeResource(IResource resource,
                              String resourceName,
                              String email,
                              ResourceType type,
                              List<ITask> tasks);

    IResource getSelectedResource();

    void setSelectedResource(IResource resource);

    ObjectProperty<IResource> selectedResourceProperty();

    void handleAddProject(String projectName,
                          LocalDate startDate,
                          LocalDate finishDate,
                          String description);

    void setProject(IProject project);
}
