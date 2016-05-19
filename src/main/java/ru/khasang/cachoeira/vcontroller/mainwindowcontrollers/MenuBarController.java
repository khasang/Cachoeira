package ru.khasang.cachoeira.vcontroller.mainwindowcontrollers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
import ru.khasang.cachoeira.commands.CommandExecutor;
import ru.khasang.cachoeira.data.DBSchemeManager;
import ru.khasang.cachoeira.data.DataStoreInterface;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.Project;
import ru.khasang.cachoeira.properties.ISettingsManager;
import ru.khasang.cachoeira.properties.RecentProjectsController;
import ru.khasang.cachoeira.properties.SettingsManager;
import ru.khasang.cachoeira.vcontroller.AboutWindowController;
import ru.khasang.cachoeira.vcontroller.CreateNewProjectWindowController;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.menubar.AbstractMenuBar;

import java.io.File;
import java.util.List;

public class MenuBarController {
    private final AbstractMenuBar menuBar;
    private final MainWindowController controller;

    public MenuBarController(AbstractMenuBar menuBar, MainWindowController controller) {
        this.menuBar = menuBar;
        this.controller = controller;
    }

    public void attachMenuBarEvents() {
        menuBar.getCreateProject().setOnAction(this::createProjectMenuItemHandler);
        menuBar.getOpenProject().setOnAction(this::openProjectMenuItemHandler);
        menuBar.getSaveProject().setOnAction(this::saveProjectMenuItemHandler);
        menuBar.getSaveAsProject().setOnAction(this::saveAsProjectMenuItemProject);
        menuBar.getExportResources().setOnAction(this::exportResourcesMenuItemHandler);
        menuBar.getImportResources().setOnAction(this::importResourcesMenuItemHandler);
        menuBar.getExit().setOnAction(this::exitMenuItemHandler);
        menuBar.getUndo().setOnAction(this::undoMenuItemHandler);
        menuBar.getRedo().setOnAction(this::redoMenuItemHandler);
        menuBar.getAbout().setOnAction(this::aboutMenuItemHandler);
    }

    private void createProjectMenuItemHandler(ActionEvent event) {
        CreateNewProjectWindowController createNewProjectWindowController = new CreateNewProjectWindowController(controller.getView());
        createNewProjectWindowController.launch();
    }

    private void openProjectMenuItemHandler(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Documents/Cachoeira"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CACH", "*.cach"));
        File file = fileChooser.showOpenDialog(controller.getView().getStage());
        if (file != null) {
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

            MainWindowController mainWindowController = new MainWindowController(file, project, new CommandExecutor());
            mainWindowController.launch();
        }
    }

    private void saveProjectMenuItemHandler(ActionEvent event) {
        //сохранение проекта
        DataStoreInterface storeInterface = new DBSchemeManager();
        storeInterface.saveProjectToFile(controller.getProjectFile(), controller.getProject());
        storeInterface.saveTasksToFile(controller.getProjectFile(), controller.getProject());
        storeInterface.saveResourcesToFile(controller.getProjectFile(), controller.getProject());
        storeInterface.saveParentTasksToFile(controller.getProjectFile(), controller.getProject());
        storeInterface.saveChildTasksToFile(controller.getProjectFile(), controller.getProject());
        storeInterface.saveResourcesByTask(controller.getProjectFile(), controller.getProject());
        // После сохранения очищаем списки анду и реду
        controller.getCommandExecutor().clearLists();
    }

    private void saveAsProjectMenuItemProject(ActionEvent event) {
        DataStoreInterface storeInterface = new DBSchemeManager();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CACH", "*.cach"));
        File file = fileChooser.showSaveDialog(controller.getView().getStage());
        if (file != null) {
            controller.setProjectFile(file);
            storeInterface.createProjectFile(file.getPath(), controller.getProject());
            storeInterface.saveProjectToFile(controller.getProjectFile(), controller.getProject());
            storeInterface.saveTasksToFile(controller.getProjectFile(), controller.getProject());
            storeInterface.saveResourcesToFile(controller.getProjectFile(), controller.getProject());
            storeInterface.saveParentTasksToFile(controller.getProjectFile(), controller.getProject());
            storeInterface.saveChildTasksToFile(controller.getProjectFile(), controller.getProject());
            storeInterface.saveResourcesByTask(controller.getProjectFile(), controller.getProject());
            // После сохранения очищаем списки анду и реду
            controller.getCommandExecutor().clearLists();
        }
    }

    private void exportResourcesMenuItemHandler(ActionEvent event) {
        DataStoreInterface storeInterface = new DBSchemeManager();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CRES", "*.cres"));
        File file = fileChooser.showSaveDialog(controller.getView().getStage());
        if (file != null) {
            storeInterface.createResourceExportFile(file);
            storeInterface.saveResourcesToFile(file, controller.getProject());
        }
    }

    private void importResourcesMenuItemHandler(ActionEvent event) {
        DataStoreInterface storeInterface = new DBSchemeManager();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CRES", "*.cres"));
        File file = fileChooser.showOpenDialog(controller.getView().getStage());
        if (file != null) {
            List<IResource> resourceListFromFile = storeInterface.getResourceListFromFile(file);
            controller.getProject().getResourceList().addAll(resourceListFromFile);
        }
    }

    private void exitMenuItemHandler(ActionEvent event) {
        this.saveProperties();
        this.saveProject(null);
    }

    private void undoMenuItemHandler(ActionEvent event) {
        controller.getCommandExecutor().undo();
    }

    private void redoMenuItemHandler(ActionEvent event) {
        controller.getCommandExecutor().redo();
    }

    private void aboutMenuItemHandler(ActionEvent event) {
        AboutWindowController aboutWindowController = new AboutWindowController();
        aboutWindowController.launch();
    }

    private void saveProperties() {
        ISettingsManager settingsManager = SettingsManager.getInstance();
        settingsManager.writeUIValues(
                controller.getSplitPaneDividerValue(),
                controller.getZoomMultiplier(),
                controller.getView().getStage().getWidth(),
                controller.getView().getStage().getHeight(),
                controller.getView().getStage().isMaximized());
        settingsManager.writeRecentProjects(RecentProjectsController.getInstance().getRecentProjects());
    }

    private void saveProject(WindowEvent event) {
        //минимум JDK 8u40
        if (controller.getCommandExecutor().isChanged()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cachoeira");
            alert.setHeaderText("Вы хотите сохранить изменения в " + controller.getProject().getName() + "?");

            ButtonType saveProjectButtonType = new ButtonType("Сохранить");
            ButtonType doNotSaveProjectButtonType = new ButtonType("Не сохранять");
            ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(saveProjectButtonType, doNotSaveProjectButtonType, cancelButtonType);

            alert.showAndWait().ifPresent(response -> {
                if (response == saveProjectButtonType) {
                    DataStoreInterface storeInterface = new DBSchemeManager();
                    storeInterface.saveProjectToFile(controller.getProjectFile(), controller.getProject());
                    storeInterface.saveTasksToFile(controller.getProjectFile(), controller.getProject());
                    storeInterface.saveResourcesToFile(controller.getProjectFile(), controller.getProject());
                    storeInterface.saveParentTasksToFile(controller.getProjectFile(), controller.getProject());
                    storeInterface.saveChildTasksToFile(controller.getProjectFile(), controller.getProject());
                    storeInterface.saveResourcesByTask(controller.getProjectFile(), controller.getProject());

                    controller.getView().getStage().close();
                } else if (response == doNotSaveProjectButtonType) {
                    controller.getView().getStage().close();
                } else {
                    if (event != null) {
                        event.consume();
                    }
                }
            });
        } else {
            controller.getView().getStage().close();
        }
    }
}
