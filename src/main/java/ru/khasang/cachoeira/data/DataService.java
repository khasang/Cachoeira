package ru.khasang.cachoeira.data;

import javafx.collections.FXCollections;
import ru.khasang.cachoeira.commands.CommandExecutor;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.Project;
import ru.khasang.cachoeira.properties.RecentProjectsController;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;

import java.io.File;

public class DataService {
    private static DataService instance;
    private DataStoreInterface storeInterface;

    private DataService() {
        storeInterface = new DBSchemeManager();
    }

    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }

    public void loadProject(File projectFile) {
        if (projectFile.exists()) {
            IProject project = storeInterface.getProjectFromFile(projectFile, new Project());
            project.setResourceList(FXCollections.observableArrayList(storeInterface.getResourceListFromFile(projectFile)));
            project.setTaskList(FXCollections.observableArrayList(storeInterface.getTaskListFromFile(projectFile)));
            for (ITask task : project.getTaskList()) {
                task.setResourceList(FXCollections.observableArrayList(storeInterface.getResourceListByTaskFromFile(projectFile, project, task)));
            }
            for (ITask task : project.getTaskList()) {
                task.setParentTasks(FXCollections.observableArrayList(storeInterface.getParentTaskListByTaskFromFile(projectFile, project, task)));
            }
            for (ITask task : project.getTaskList()) {
                task.setChildTasks(FXCollections.observableArrayList(storeInterface.getChildTaskListByTaskFromFile(projectFile, project, task)));
            }

            RecentProjectsController.getInstance().addRecentProject(projectFile);

            MainWindowController mainWindowController = new MainWindowController(projectFile, project, new CommandExecutor());
            mainWindowController.launch();
            mainWindowController.refreshInformation();
        }
    }

    public void createProject(IProject project, File newProjectFile) {
        // Создаем файл
        storeInterface.createProjectFile(newProjectFile.getPath(), project);
        // Очищаем файл проекта на тот случай, если файл перезаписывается
        storeInterface.eraseAllTables(newProjectFile);
        storeInterface.saveProjectToFile(newProjectFile, project);
        RecentProjectsController.getInstance().addRecentProject(newProjectFile);
        MainWindowController mainWindowController = new MainWindowController(newProjectFile, project, new CommandExecutor());
        mainWindowController.launch();
    }

    public void saveProject(File projectFile, IProject project) {
        //сохранение проекта
        storeInterface.saveProjectToFile(projectFile, project);
        storeInterface.saveTasksToFile(projectFile, project);
        storeInterface.saveResourcesToFile(projectFile, project);
        storeInterface.saveParentTasksToFile(projectFile, project);
        storeInterface.saveChildTasksToFile(projectFile, project);
        storeInterface.saveResourcesByTask(projectFile, project);
    }

    public void saveProjectAs(File projectFile, IProject project) {
        storeInterface.createProjectFile(projectFile.getPath(), project);
        storeInterface.saveProjectToFile(projectFile, project);
        storeInterface.saveTasksToFile(projectFile, project);
        storeInterface.saveResourcesToFile(projectFile, project);
        storeInterface.saveParentTasksToFile(projectFile, project);
        storeInterface.saveChildTasksToFile(projectFile, project);
        storeInterface.saveResourcesByTask(projectFile, project);
        RecentProjectsController.getInstance().addRecentProject(projectFile);
    }
}
