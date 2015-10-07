package ru.khasang.cachoeira.model;

import java.util.Date;

/**
 * Created by Raenar on 07.10.2015.
 */
public class Task implements ITask {
    private String name;
    private Date start;
    private Date finish;
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
    public int getDonePercent() {
        return donePercent;
    }

    @Override
    public void setDonePercent(int donePercent) {
        this.donePercent = donePercent;
    }
}
