package ru.khasang.cachoeira.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.input.DataFormat;
import ru.khasang.cachoeira.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by truesik on 22.10.2015.
 */
public class Controller implements IController {
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    private IProject project;
    private ITask task;
    private ObjectProperty<ITask> selectedTask = new SimpleObjectProperty<>(this, "selectedTask");
    private IResource resource;
    private ObjectProperty<IResource> selectedResource = new SimpleObjectProperty<>(this, "selectedResource");
    private List<IProject> projectList = new ArrayList<>(); //по архитектуре не понял где у нас должен храниться список проектов, поэтому пускай пока будет здесь

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

    //добавил:
    @Override
    public void handleAddTask(ITask task) {
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

    //Добавил:
    @Override
    public void handleChangeTask(String taskNameField, LocalDate taskStartDate, LocalDate taskFinishDate, Double taskCost, double taskDonePercent, PriorityType priority, ObservableList<IResource> resources) { //todo тоже подправил
        task.setName(taskNameField);
        task.setStartDate(taskStartDate);
        task.setFinishDate(taskFinishDate);
        task.setCost(taskCost);
        task.setPriorityType(priority);
        task.setDonePercent((int) taskDonePercent);
        task.setResourceList(resources);
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

    @Override
    public void handleAddResource(IResource resource) {
        project.getResourceList().add(resource);
    }

    @Override
    public void notifyAddResource(IResource resource) {

    }

    @Override
    public void handleRemoveResource(IResource resource) {
        /** Удаляем этот ресурс из всех привязанных к нему задач */
        for (ITask iTask : getProject().getTaskList()) {
            if (iTask.getResourceList().contains(resource)) {
                iTask.getResourceList().remove(resource);
            }
        }
        /** Удялем этот ресурс из проекта */
        project.getResourceList().remove(resource);
    }

    @Override
    public void notifyRemoveResource(IResource resource) {

    }

    @Override
    public void handleChangeResource(String resourceName, String email, ResourceType type, List<ITask> tasks) {
        resource.setName(resourceName);
        resource.setEmail(email);
        resource.setType(type);

        for (ITask t : project.getTaskList()) {
            for (ITask tas : tasks) {
                if (!t.equals(tas) && t.getResourceList().contains(resource)) {
                    t.removeResource(resource);
                }
                if (!tas.getResourceList().contains(resource)) {
                    tas.addResource(resource);
                }
            }
        }
    }

    @Override
    public void notifyChangeResource(IResource resource) {

    }

    @Override
    public IResource getSelectedResource() {
        return resource;
    }

    @Override
    public void setSelectedResource(IResource resource) {
        this.resource = resource;
    }

    @Override
    public void notifyAddProject(String projectName, LocalDate startDate, LocalDate finishDate, String description) {
        project = new Project();
        project.setName(projectName);
        project.setStartDate(startDate);
        project.setFinishDate(finishDate);
        project.setDescription(description);
        projectList.add(project);
        setProject(project);
    }

    @Override
    public void setProject(IProject project) {
        this.project = project;
    }

    public static DataFormat getSerializedMimeType() {
        return SERIALIZED_MIME_TYPE;
    }

    public ObjectProperty<ITask> selectedTaskProperty() {
        return selectedTask;
    }

    public ObjectProperty<IResource> selectedResourceProperty() {
        return selectedResource;
    }
}
