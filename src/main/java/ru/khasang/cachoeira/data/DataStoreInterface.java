package ru.khasang.cachoeira.data;

import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

import java.io.File;
import java.util.List;

public interface DataStoreInterface {
    void createProjectFile(String path);

    void saveProjectToFile(File file);

    List<ITask> getTaskListFromFile(File file);

    List<IResource> getResourceListFromFile(File file);
}
