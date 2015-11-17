package ru.khasang.cachoeira.model;

import com.sun.xml.internal.stream.Entity;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.util.Date;
import java.util.List;

/**
 * Created by nadezhda on 07.10.2015.
 */
public interface IProject {
    String getName();

    void setName(String nameDate);

    StringProperty nameProperty();

    Date getStartDate();

    void setStartDate(Date startDate);

    ObjectProperty<Date> startDateProperty();

    Date getFinishDate();

    void setFinishDate(Date finishDate);

    ObjectProperty<Date> finishDateProperty();

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
