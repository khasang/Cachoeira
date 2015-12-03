package ru.khasang.cachoeira.model;

import javafx.beans.property.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by truesik on 22.10.2015.
 */
public class Resource implements IResource {
    private final ReadOnlyIntegerWrapper id = new ReadOnlyIntegerWrapper(this, "id", resourceSequence.incrementAndGet());
    private StringProperty name = new SimpleStringProperty(this, "name");
    private ObjectProperty<ResourceType> type = new SimpleObjectProperty<>(this, "type");
    private StringProperty email = new SimpleStringProperty(this, "email");
    private StringProperty description = new SimpleStringProperty(this, "description");

    /** Запоминаем количество задач **/
    private static AtomicInteger resourceSequence = new AtomicInteger(0);

    /** Конструктор с дефолтовыми значениями **/
    public Resource() {
        this.name.setValue("Ресурс " + id.getValue());
        this.type.setValue(ResourceType.STUFF);
    }

    @Override
    public int getId() {
        return id.get();
    }

    @Override
    public ReadOnlyIntegerProperty idProperty() {
        return id.getReadOnlyProperty();
    }

    @Override
    public void setId(int id) {
        this.id.set(id);
    }

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

    @Override
    public final String getDescription() {
        return description.get();
    }

    @Override
    public final void setDescription(String description) {
        this.description.set(description);
    }

    @Override
    public final StringProperty descriptionProperty() {
        return description;
    }
}
