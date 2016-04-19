package ru.khasang.cachoeira.model;

import javafx.beans.property.StringProperty;

public interface ITaskGroup {
    String getName();

    void setName(String name);

    StringProperty nameProperty();
}
