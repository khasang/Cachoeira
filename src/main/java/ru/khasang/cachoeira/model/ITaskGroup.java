package ru.khasang.cachoeira.model;

import javafx.beans.property.StringProperty;

/**
 * Created by truesik on 22.10.2015.
 */
public interface ITaskGroup {
    String getName();

    void setName(String name);

    StringProperty nameProperty();
}
