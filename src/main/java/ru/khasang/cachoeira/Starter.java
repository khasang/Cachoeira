package ru.khasang.cachoeira;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.data.DBSchemeManager;
import ru.khasang.cachoeira.data.DataStoreInterface;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.Project;
import ru.khasang.cachoeira.properties.RecentProjectsController;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.vcontroller.StartWindowController;

import java.io.File;
import java.util.List;


/**
 * Этот класс является точкой входа
 */
public class Starter extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Starter.class.getName());

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.debug("Программа запущена.");
        List<String> projectsFilePath = getParameters().getRaw();
        if (!projectsFilePath.isEmpty()) {
            for (String projectFilePath : projectsFilePath) {
                File file = new File(projectFilePath);
                if (file.exists()) {
                    DataStoreInterface storeInterface = new DBSchemeManager();
                    IProject project = storeInterface.getProjectFromFile(file, new Project());
                    project.setResourceList(FXCollections.observableArrayList(storeInterface.getResourceListFromFile(file)));
                    project.setTaskList(FXCollections.observableArrayList(storeInterface.getTaskListFromFile(file)));
                    for (ITask task : project.getTaskList()) {
                        task.setResourceList(FXCollections.observableArrayList(storeInterface.getResourceListByTaskFromFile(file, project, task)));
                    }
                    for (ITask task : project.getTaskList()) {
                        task.setParentTasks(FXCollections.observableArrayList(storeInterface.getParentTaskListByTaskFromFile(file, project, task)));
                    }
                    for (ITask task : project.getTaskList()) {
                        task.setChildTasks(FXCollections.observableArrayList(storeInterface.getChildTaskListByTaskFromFile(file, project, task)));
                    }

                    RecentProjectsController.getInstance().addRecentProject(file);

                    MainWindowController mainWindowController = new MainWindowController(file, project);
                    mainWindowController.launch();
                }
            }
        } else {
            StartWindowController startWindowController = new StartWindowController();
            startWindowController.launch();
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
