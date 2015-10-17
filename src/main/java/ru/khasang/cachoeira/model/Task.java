package ru.khasang.cachoeira.model;

import java.util.Date;

/**
 * Created by Raenar on 07.10.2015.
 */
public class Task implements ITask {
    private String name;
    private Date startDate;
    private Date finishDate;
    private int donePercent;

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
    public void setStartDate(Date start) {
        this.startDate = start;
    }

    @Override
    public Date getFinishDate() {
        return finishDate;
    }

    @Override
    public void setFinishDate(Date finish) {
        this.finishDate = finish;
    }

    @Override
    public int getDonePercent() {
        return donePercent;
    }

    @Override
    public void setDonePercent(int donePercent) {
        this.donePercent = donePercent;
    }
}
