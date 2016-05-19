package ru.khasang.cachoeira.data;

import ru.khasang.cachoeira.model.IDependentTask;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

import java.io.File;
import java.util.List;

public interface DataStoreInterface {
    void createProjectFile(String path, IProject project);

    void createResourceExportFile(File file);

    void saveProjectToFile(File file, IProject project);

    void saveTasksToFile(File file, IProject project);

    void saveResourcesToFile(File file, IProject project);

    void saveParentTasksToFile(File file, IProject project);

    void saveChildTasksToFile(File file, IProject project);

    void saveResourcesByTask(File file, IProject project);

    List<ITask> getTaskListFromFile(File file);

    List<IResource> getResourceListByTaskFromFile(File file, IProject project, ITask task);

    List<IDependentTask> getParentTaskListByTaskFromFile(File file, IProject project, ITask task);

    List<IDependentTask> getChildTaskListByTaskFromFile(File file, IProject project, ITask task);

    List<IResource> getResourceListFromFile(File file);

    IProject getProjectFromFile(File file, IProject project);

    void eraseAllTables(File file);
}
