package ru.khasang.cachoeira.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TaskGroup implements ITaskGroup {
    StringProperty name = new SimpleStringProperty(this, "name");

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
