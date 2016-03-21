package ru.khasang.cachoeira.data;

import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

import java.util.List;

public interface DataStoreInterface {
    void createProjectFile();

    void saveProjectToFile();

    List<ITask> getTaskListFromFile();

    List<IResource> getResourceListFromFile();
}
