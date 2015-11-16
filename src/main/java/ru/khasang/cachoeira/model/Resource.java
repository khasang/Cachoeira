package ru.khasang.cachoeira.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by truesik on 22.10.2015.
 */
public class Resource implements IResource {
    private StringProperty name = new SimpleStringProperty();
    private ObjectProperty<ResourceType> type = new SimpleObjectProperty<>();
    private StringProperty email = new SimpleStringProperty();

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public ResourceType getType() {
        return type.get();
    }

    @Override
    public void setType(ResourceType type) {
        this.type.set(type);
    }

    @Override
    public ObjectProperty<ResourceType> resourceTypeProperty() {
        return type;
    }

    @Override
    public String getEmail() {
        return email.get();
    }

    @Override
    public void setEmail(String email) {
        this.email.set(email);
    }

    @Override
    public StringProperty emailProperty() {
        return email;
    }
}
