package ru.khasang.cachoeira.controller;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import ru.khasang.cachoeira.model.*;

import java.time.LocalDate;
import java.util.List;

public interface IController {
    IProject getDefaultProject();

    IProject getProject();

    ITask getSelectedTask();

    void setSelectedTask(ITask task);

    ObjectProperty<ITask> selectedTaskProperty();

    IResource getSelectedResource();

    void setSelectedResource(IResource resource);

    ObjectProperty<IResource> selectedResourceProperty();

    void handleAddProject(String projectName,
                          LocalDate startDate,
                          LocalDate finishDate,
                          String description);

    void setProject(IProject project);
}
