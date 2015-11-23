package ru.khasang.cachoeira.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by truesik on 22.10.2015.
 */
public class TaskGroup implements ITaskGroup {
    StringProperty name = new SimpleStringProperty();

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public StringProperty nameProperty() {
        return name;
    }
}
