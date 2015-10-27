package ru.khasang.cachoeira.model;

import com.sun.xml.internal.stream.Entity;

import java.util.Date;
import java.util.List;

/**
 * Created by nadezhda on 07.10.2015.
 */
public interface IProject {
    String getName();

    void setName(String nameDate);

    Date getStartDate();

    void setStartDate(Date startDate);

    Date getFinishDate();

    void setFinishDate(Date finishDate);

    List<ITask> getTaskList(); //Интерфейс ITask будет создаваться в другой задаче

    void setTaskList(List<ITask> tasks);

    List<IResource> getResourceList();

    void setResourceList(List<IResource> resources);

//    void save(String target); //Метод, сохраняющий проект в хранилище
//    void load(String source); //Метод, загружающий проект из хранилища
//    void exportResources(String target); //Метод, выгружающий ресурсы проекта во внешний файл
//    void importResources(String sources); //Метод, добавляющий новые ресурсы из внешнего файла
}
