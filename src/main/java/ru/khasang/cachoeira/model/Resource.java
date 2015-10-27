package ru.khasang.cachoeira.model;

/**
 * Created by truesik on 22.10.2015.
 */
public class Resource implements IResource {
    private String name;
    private ResourceType type;
    private String email;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ResourceType getType() {
        return type;
    }

    @Override
    public void setType(ResourceType type) {
        this.type = type;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }
}
