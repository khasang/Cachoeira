package ru.khasang.cachoeira.model;

import java.util.Date;
import java.util.List;

/**
 * Created by nadezhda on 07.10.2015.
 */
public class Project implements IProject {

    private String name;
    private Date start;
    private Date finish;
    private List<ITask> tasks; //Интерфейс ITask будет создаваться в другой задаче

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
    public Date getStart() {
        return start;
    }

    @Override
    public void setStart(Date start) {
        this.start = start;
    }

    @Override
    public Date getFinish() {
        return finish;
    }

    @Override
    public void setFinish(Date finish) {
        this.finish = finish;
    }

    @Override
    public List<ITask> getTaskList() {
        return tasks;
    }

    @Override
    public void setTaskList(List<ITask> tasks) {
        this.tasks = tasks;
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
