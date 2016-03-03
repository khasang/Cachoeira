package ru.khasang.cachoeira.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.input.DataFormat;
import ru.khasang.cachoeira.model.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Класс-контроллер между представлением и моделью.
 */
public class Controller implements IController {
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    private IProject project;
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

    @Override
    public void handleChangeProject(IProject project) {

    }

    @Override
    public void handleAddTask(ITask task) {
        project.getTaskList().add(task);
    }

    @Override
    public void handleRemoveTask(ITask task) {
        // Вычищяем все задачи из списка предшествующих задач
        task.getParentTasks().clear();
        // Вычищаем все задачи из списка последующих задач
        task.getChildTasks().clear();
        // Вычищяем все ресурсы из списка привязанных ресурсов
        task.getResourceList().clear();
        // Удаляем все связи с этой задачей
        project.getTaskList().stream()
                .forEach(taskFromList -> taskFromList.getParentTasks()
                        .removeIf(dependentTask -> dependentTask.getTask().equals(task)));
        project.getTaskList().stream()
                .forEach(taskFromList -> taskFromList.getChildTasks()
                        .removeIf(dependentTask -> dependentTask.getTask().equals(task)));
        // Удаляем эту задачу
        project.getTaskList().remove(task);
    }

    @Override
    public void handleChangeTask(ITask task,
                                 String taskNameField,
                                 LocalDate taskStartDate,
                                 LocalDate taskFinishDate,
                                 int duration,
                                 Double taskCost,
                                 double taskDonePercent,
                                 PriorityType priority,
                                 ObservableList<IResource> resources) {
        task.setName(taskNameField);
        task.setStartDate(taskStartDate);
        task.setFinishDate(taskFinishDate);
        task.setCost(taskCost);
        task.setPriorityType(priority);
        task.setDonePercent((int) taskDonePercent);
        task.setResourceList(resources);
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
    public void handleAddResource(IResource resource) {
        project.getResourceList().add(resource);
    }

    @Override
    public void handleRemoveResource(IResource resource) {
        // Удаляем этот ресурс из всех привязанных к нему задач
        getProject().getTaskList()
                .stream()
                .filter(task -> task.getResourceList().contains(resource))
                .forEach(task -> task.getResourceList().remove(resource));
        // Удаляем этот ресурс из проекта
        project.getResourceList().remove(resource);
    }

    @Override
    public void handleChangeResource(IResource resource,
                                     String resourceName,
                                     String email,
                                     ResourceType type,
                                     List<ITask> tasks) {
        resource.setName(resourceName);
        resource.setEmail(email);
        resource.setType(type);

        for (ITask task : project.getTaskList()) {
            for (ITask tas : tasks) {
                if (!task.equals(tas) && task.getResourceList().contains(resource)) {
                    task.removeResource(resource);
                }
                if (!tas.getResourceList().contains(resource)) {
                    tas.addResource(resource);
                }
            }
        }
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
        project = new Project();
        project.setName(projectName);
        project.setStartDate(startDate);
        project.setFinishDate(finishDate);
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
