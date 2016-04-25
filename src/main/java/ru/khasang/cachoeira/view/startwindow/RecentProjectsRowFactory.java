package ru.khasang.cachoeira.view.startwindow;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import ru.khasang.cachoeira.data.DBSchemeManager;
import ru.khasang.cachoeira.data.DataStoreInterface;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.properties.RecentProjectsController;
import ru.khasang.cachoeira.view.UIControl;

import java.io.File;

public class RecentProjectsRowFactory implements Callback<TableView<File>, TableRow<File>> {
    private UIControl uiControl;

    public RecentProjectsRowFactory(UIControl uiControl) {
        this.uiControl = uiControl;
    }

    @Override
    public TableRow<File> call(TableView<File> param) {
        TableRow<File> row = new TableRow<>();
        row.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2 && !row.isEmpty()) {
                File recentProject = row.getItem();
                if (recentProject.exists()) {
                    uiControl.setFile(recentProject);
                    DataStoreInterface storeInterface = new DBSchemeManager(uiControl);
                    IProject project = storeInterface.getProjectFromFile(uiControl.getFile(), uiControl.getController().getProject());
                    uiControl.getController().handleAddProject(project.getName(), project.getStartDate(), project.getFinishDate(), project.getDescription());
                    uiControl.getController().getProject().setResourceList(FXCollections.observableArrayList(storeInterface.getResourceListFromFile(uiControl.getFile())));
                    uiControl.getController().getProject().setTaskList(FXCollections.observableArrayList(storeInterface.getTaskListFromFile(uiControl.getFile())));
                    for (ITask task : uiControl.getController().getProject().getTaskList()) {
                        task.setResourceList(FXCollections.observableArrayList(storeInterface.getResourceListByTaskFromFile(uiControl.getFile(), task)));
                    }
                    for (ITask task : uiControl.getController().getProject().getTaskList()) {
                        task.setParentTasks(FXCollections.observableArrayList(storeInterface.getParentTaskListByTaskFromFile(uiControl.getFile(), task)));
                    }
                    for (ITask task : uiControl.getController().getProject().getTaskList()) {
                        task.setChildTasks(FXCollections.observableArrayList(storeInterface.getChildTaskListByTaskFromFile(uiControl.getFile(), task)));
                    }

                    RecentProjectsController.getInstance().addRecentProject(uiControl.getFile());
                    if (uiControl.getStartWindow().getStage().isShowing()) {
                        uiControl.getStartWindow().getStage().close(); //закрываем стартовое окно
                    }
                    uiControl.launchMainWindow();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "File does not exist");
                    alert.showAndWait();
                    RecentProjectsController.getInstance().removeRecentProject(recentProject);
                }
            }
        });
        return row;
    }
}
