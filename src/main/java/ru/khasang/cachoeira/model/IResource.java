package ru.khasang.cachoeira.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by truesik on 22.10.2015.
 */
public interface IResource {
    int getId();

    ReadOnlyIntegerProperty idProperty();

    void setId(int id);

    String getName();

    void setName(String name);

    StringProperty nameProperty();

    ResourceType getType();

    void setType(ResourceType type);

    ObjectProperty<ResourceType> resourceTypeProperty();

    String getEmail();

    void setEmail(String email);

    StringProperty emailProperty();
}
