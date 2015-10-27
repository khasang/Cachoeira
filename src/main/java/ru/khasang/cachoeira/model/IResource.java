package ru.khasang.cachoeira.model;

/**
 * Created by truesik on 22.10.2015.
 */
public interface IResource {
    String getName();
    void setName(String name);
    ResourceType getType();
    void setType(ResourceType type);
    String getEmail();
    void setEmail(String email);
}
