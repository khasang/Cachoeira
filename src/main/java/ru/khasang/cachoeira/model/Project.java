package ru.khasang.cachoeira.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nadezhda on 07.10.2015.
 */
public class Project implements IProject {

    private String name;
    private Date startDate;
    private Date finishDate;
    private List<ITask> tasks = new ArrayList<>(); //Интерфейс ITask будет создаваться в другой задаче
    private List<IResource> resources = new ArrayList<>();

    public Project() {
    }

    public Project(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public Date getFinishDate() {
        return finishDate;
    }

    @Override
    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    @Override
    public List<ITask> getTaskList() {
        return tasks;
    }

    @Override
    public void setTaskList(List<ITask> tasks) {
        this.tasks = tasks;
    }

    @Override
    public List<IResource> getResourceList() {
        return resources;
    }

    @Override
    public void setResourceList(List<IResource> resources) {
        this.resources = resources;
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
