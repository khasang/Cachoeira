package ru.khasang.cachoeira.model;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by nadezhda on 07.10.2015.
 */
public class Project implements IProject {

    private StringProperty name = new SimpleStringProperty(this, "name");
    private ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>(this, "startDate");
    private ObjectProperty<LocalDate> finishDate = new SimpleObjectProperty<>(this, "finishDate");
    private StringProperty description = new SimpleStringProperty(this, "description");
    private ObservableList<ITask> tasks = FXCollections.observableArrayList(new Callback<ITask, Observable[]>() {
        @Override
        public Observable[] call(ITask param) {
            return new Observable[] {
                    param.nameProperty(),
                    param.startDateProperty(),
                    param.finishDateProperty(),
                    param.donePercentProperty(),
                    param.durationProperty(),
                    param.priorityTypeProperty(),
                    param.costProperty(),
                    param.getDependentTasks(),
                    param.groupProperty(),
                    param.getResourceList()
            };
        }
    });
    private ObservableList<IResource> resources = FXCollections.observableArrayList(new Callback<IResource, Observable[]>() {
        @Override
        public Observable[] call(IResource param) {
            return new Observable[] {
                    param.nameProperty(),
                    param.resourceTypeProperty(),
                    param.emailProperty()
            };
        }
    });

    public Project() {
    }

    public Project(String name) {
        this.name.set(name);
    }

    @Override
    public final String getName() {
        return name.get();
    }

    @Override
    public final void setName(String name) {
        this.name.set(name);
    }

    @Override
    public final StringProperty nameProperty() {
        return name;
    }

    @Override
    public final LocalDate getStartDate() {
        return startDate.get();
    }

    @Override
    public final void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
    }

    @Override
    public final ObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }

    @Override
    public final LocalDate getFinishDate() {
        return finishDate.get();
    }

    @Override
    public final void setFinishDate(LocalDate finishDate) {
        this.finishDate.set(finishDate);
    }

    @Override
    public final ObjectProperty<LocalDate> finishDateProperty() {
        return finishDate;
    }

    @Override
    public final ObservableList<ITask> getTaskList() {
        return tasks;
    }

    @Override
    public final void setTaskList(ObservableList<ITask> tasks) {
        this.tasks = tasks;
    }

    @Override
    public final ObservableList<IResource> getResourceList() {
        return resources;
    }

    @Override
    public final void setResourceList(ObservableList<IResource> resources) {
        this.resources = resources;
    }

    @Override
    public final String getDescription() {
        return description.get();
    }

    @Override
    public final void setDescription(String description) {
        this.description.set(description);
    }

    @Override
    public final StringProperty descriptionProperty() {
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
