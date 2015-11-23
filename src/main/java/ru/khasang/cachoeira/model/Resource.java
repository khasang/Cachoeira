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
    public final String getName() {
        return name.get();
    }

    @Override
    public final void setName(String name) {
        this.name.set(name);
    }

    @Override
    public final StringProperty nameProperty() {
        return name;
    }

    @Override
    public final ResourceType getType() {
        return type.get();
    }

    @Override
    public final void setType(ResourceType type) {
        this.type.set(type);
    }

    @Override
    public final ObjectProperty<ResourceType> resourceTypeProperty() {
        return type;
    }

    @Override
    public final String getEmail() {
        return email.get();
    }

    @Override
    public final void setEmail(String email) {
        this.email.set(email);
    }

    @Override
    public final StringProperty emailProperty() {
        return email;
    }
}
