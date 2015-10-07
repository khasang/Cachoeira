package ru.khasang.cachoeira.model;

import java.util.Date;

/**
 * Created by Raenar on 07.10.2015.
 */
public interface ITask {
    String getName();
    void setName(String name);
    Date getStart();
    void setStart(Date start);
    Date getFinish();
    void setFinish(Date finish);
    int getDonePercent();
    void setDonePercent(int donePercent);
}
