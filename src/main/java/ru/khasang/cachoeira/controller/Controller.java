package ru.khasang.cachoeira.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.DataFormat;
import ru.khasang.cachoeira.model.*;

import java.time.LocalDate;

/**
 * Класс-контроллер между представлением и моделью.
 */
public class Controller implements IController {
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    private IProject project = new Project();
    private ObjectProperty<ITask> selectedTask = new SimpleObjectProperty<>(this, "selectedTask", null);
    private ObjectProperty<IResource> selectedResource = new SimpleObjectProperty<>(this, "selectedResource", null);

    @Override
    public IProject getDefaultProject() {
        return null;
    }

    @Override
    public IProject getProject() {
        return project;
    }

    public ITask getSelectedTask() {
        return selectedTask.get();
    }

    public void setSelectedTask(ITask task) {
        this.selectedTask.set(task);
    }

    public ObjectProperty<ITask> selectedTaskProperty() {
        return selectedTask;
    }

    @Override
    public IResource getSelectedResource() {
        return selectedResource.get();
    }

    @Override
    public void setSelectedResource(IResource resource) {
        selectedResource.set(resource);
    }

    public ObjectProperty<IResource> selectedResourceProperty() {
        return selectedResource;
    }

    @Override
    public void handleAddProject(String projectName,
                                 LocalDate startDate,
                                 LocalDate finishDate,
                                 String description) {
        if (project == null) {
            project = new Project();
        }
        project.setName(projectName);
        project.setStartDateAndVerify(startDate);
        project.setFinishDateAndVerify(finishDate);
        project.setDescription(description);
        setProject(project);
    }

    @Override
    public void setProject(IProject project) {
        this.project = project;
    }

    public static DataFormat getSerializedMimeType() {
        return SERIALIZED_MIME_TYPE;
    }
}
