package ru.khasang.cachoeira.controller;

import ru.khasang.cachoeira.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by truesik on 22.10.2015.
 */
public class Controller implements IController {
    private IProject project;
    private ITask task;
    private IResource resource;
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

    @Override
    public void handleAddTask(String nameOfTask, Date startDate, Date finishDate, List<IResource> resources) { //todo в оригинале параметр был ITask task, но я не понял как так сделать, возможно кто-нибудь поправит
        task = new Task();
        task.setName(nameOfTask);
        task.setStartDate(startDate);
        task.setFinishDate(finishDate);
        task.setResourceList(resources);
        project.getTaskList().add(task);
        System.out.println(task.getResourceList());
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
    public void handleChangeTask(String taskNameField, Date taskStartDate, Date taskFinishDate, List<IResource> resources) { //todo тоже подправил
        task.setName(taskNameField);
        task.setStartDate(taskStartDate);
        task.setFinishDate(taskFinishDate);
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
    public void handleAddResource(String resourceName, String email, ResourceType type, List<ITask> tasks) {
        resource = new Resource();
        resource.setName(resourceName);
        resource.setEmail(email);
        resource.setType(type);
        project.getResourceList().add(resource);
        for (ITask t : tasks) {
            t.addResource(resource); //присваиваем этот ресурс задаче из списка tasks
        }
    }

    @Override
    public void notifyAddResource(IResource resource) {

    }

    @Override
    public void handleRemoveResource(IResource resource) {
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
    public void notifyAddProject(String projectName, Date startDate, Date finishDate, String description) {
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
}
