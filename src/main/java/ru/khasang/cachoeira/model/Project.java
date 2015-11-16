package ru.khasang.cachoeira.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nadezhda on 07.10.2015.
 */
public class Project implements IProject {

    private StringProperty name = new SimpleStringProperty();
    private ObjectProperty<Date> startDate = new SimpleObjectProperty<>();
    private ObjectProperty<Date> finishDate = new SimpleObjectProperty<>();
    private StringProperty description = new SimpleStringProperty();
    private ObservableList<ITask> tasks = FXCollections.observableArrayList();
    private ListProperty<ITask> taskListProperty = new SimpleListProperty<>(tasks);
    private ObservableList<IResource> resources = FXCollections.observableArrayList();
    private ListProperty<IResource> resourceListProperty = new SimpleListProperty<>(resources);

    public Project() {
    }

    public Project(String name) {
        this.name.set(name);
    }

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public Date getStartDate() {
        return startDate.get();
    }

    @Override
    public void setStartDate(Date startDate) {
        this.startDate.set(startDate);
    }

    @Override
    public ObjectProperty<Date> startDateProperty() {
        return startDate;
    }

    @Override
    public Date getFinishDate() {
        return finishDate.get();
    }

    @Override
    public void setFinishDate(Date finishDate) {
        this.finishDate.set(finishDate);
    }

    @Override
    public ObjectProperty<Date> finishDateProperty() {
        return finishDate;
    }

    @Override
    public ObservableList<ITask> getTaskList() {
        return tasks;
    }

    @Override
    public void setTaskList(ObservableList<ITask> tasks) {
        this.tasks = tasks;
    }

    @Override
    public ListProperty<ITask> taskListProperty() {
        return taskListProperty;
    }

    @Override
    public ObservableList<IResource> getResourceList() {
        return resources;
    }

    @Override
    public void setResourceList(ObservableList<IResource> resources) {
        this.resources = resources;
    }

    @Override
    public ListProperty<IResource> resourceListProperty() {
        return resourceListProperty;
    }

    @Override
    public String getDescription() {
        return description.get();
    }

    @Override
    public void setDescription(String description) {
        this.description.set(description);
    }

    @Override
    public StringProperty descriptionProperty() {
        return description;
    }

//
//    //Метод, сохраняющий проект в хранилище
//    @Override
//    public void save(String target) {
//
//    }
//
//    //Метод, загружающий проект из хранилища
//    @Override
//    public void load(String source) {
//
//    }
//
//    //Метод, выгружающий ресурсы проекта во внешний файл
//    @Override
//    public void exportResources(String target){
//
//    }
//
//    //Метод, добавляющий новые ресурсы из внешнего файла
//    @Override
//    public void importResources(String sources){
//
//    }

}
