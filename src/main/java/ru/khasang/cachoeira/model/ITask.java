package ru.khasang.cachoeira.model;

import java.util.Date;

/**
 * Created by Raenar on 07.10.2015.
 */
public interface ITask {
    String getName();
    void setName(String name);
    Date getStartDate();
    void setStartDate(Date start);
    Date getFinishDate();
    void setFinishDate(Date finish);
    int getDonePercent();
    void setDonePercent(int donePercent);
}
