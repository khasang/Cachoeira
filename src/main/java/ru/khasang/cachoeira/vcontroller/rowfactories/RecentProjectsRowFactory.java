package ru.khasang.cachoeira.vcontroller.rowfactories;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import ru.khasang.cachoeira.data.DBSchemeManager;
import ru.khasang.cachoeira.data.DataStoreInterface;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.Project;
import ru.khasang.cachoeira.properties.RecentProjectsController;
import ru.khasang.cachoeira.properties.SettingsManager;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.vcontroller.StartWindowController;

import java.io.File;

public class RecentProjectsRowFactory implements Callback<TableView<File>, TableRow<File>> {
    private StartWindowController controller;

    public RecentProjectsRowFactory(StartWindowController controller) {
        this.controller = controller;
    }

    @Override
    public TableRow<File> call(TableView<File> param) {
        TableRow<File> row = new TableRow<>();
        row.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2 && !row.isEmpty()) {
                File recentProject = row.getItem();
                if (recentProject.exists()) {
                    DataStoreInterface storeInterface = new DBSchemeManager();
                    IProject project = storeInterface.getProjectFromFile(recentProject, new Project());
                    project.setResourceList(FXCollections.observableArrayList(storeInterface.getResourceListFromFile(recentProject)));
                    project.setTaskList(FXCollections.observableArrayList(storeInterface.getTaskListFromFile(recentProject)));
                    for (ITask task : project.getTaskList()) {
                        task.setResourceList(FXCollections.observableArrayList(storeInterface.getResourceListByTaskFromFile(recentProject, project, task)));
                    }
                    for (ITask task : project.getTaskList()) {
                        task.setParentTasks(FXCollections.observableArrayList(storeInterface.getParentTaskListByTaskFromFile(recentProject, project, task)));
                    }
                    for (ITask task : project.getTaskList()) {
                        task.setChildTasks(FXCollections.observableArrayList(storeInterface.getChildTaskListByTaskFromFile(recentProject, project, task)));
                    }

                    RecentProjectsController.getInstance().addRecentProject(recentProject);
                    if (controller.getView().getStage().isShowing()) {
                        controller.getView().getStage().close(); //закрываем стартовое окно
                    }
                    MainWindowController mainWindowController = new MainWindowController(project);
                    mainWindowController.launch();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "File does not exist");
                    alert.showAndWait();
                    RecentProjectsController.getInstance().removeRecentProject(recentProject);
                    SettingsManager.getInstance().writeRecentProjects(RecentProjectsController.getInstance().getRecentProjects());
                }
            }
        });
        return row;
    }
}
