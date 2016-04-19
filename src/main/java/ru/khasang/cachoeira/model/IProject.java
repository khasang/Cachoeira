package ru.khasang.cachoeira.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public interface IProject {
    String getName();

    void setName(String nameDate);

    StringProperty nameProperty();

    LocalDate getStartDate();

    void setStartDate(LocalDate startDate);

    ObjectProperty<LocalDate> startDateProperty();

    LocalDate getFinishDate();

    void setFinishDate(LocalDate finishDate);

    ObjectProperty<LocalDate> finishDateProperty();

    ObservableList<ITask> getTaskList();

    void setTaskList(ObservableList<ITask> tasks);

    ObservableList<IResource> getResourceList();

    void setResourceList(ObservableList<IResource> resources);

    String getDescription();

    void setDescription(String description);

    StringProperty descriptionProperty();

//    void save(String target); //Метод, сохраняющий проект в хранилище
//    void load(String source); //Метод, загружающий проект из хранилища
//    void exportResources(String target); //Метод, выгружающий ресурсы проекта во внешний файл
//    void importResources(String sources); //Метод, добавляющий новые ресурсы из внешнего файла
}
