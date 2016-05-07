package ru.khasang.cachoeira.vcontroller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import ru.khasang.cachoeira.data.DBSchemeManager;
import ru.khasang.cachoeira.data.DataStoreInterface;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.Project;
import ru.khasang.cachoeira.properties.RecentProjectsController;
import ru.khasang.cachoeira.vcontroller.rowfactories.RecentProjectsRowFactory;
import ru.khasang.cachoeira.view.IView;
import ru.khasang.cachoeira.view.startwindow.ButtonsBox;
import ru.khasang.cachoeira.view.startwindow.IButtonsBox;
import ru.khasang.cachoeira.view.startwindow.StartWindowView;

import java.io.File;

public class StartWindowController {
    private final IView view;

    private TableView<File> recentProjectTable;
    private IButtonsBox buttonsBox;

    public StartWindowController() {
        recentProjectTable = new TableView<>();
        buttonsBox = new ButtonsBox();
        view = new StartWindowView(this, recentProjectTable, buttonsBox);
    }

    public void launch() {
        view.createView();
        this.setTableItemsAndRowFactory();
        this.attachButtonsEvents();
    }

    private void setTableItemsAndRowFactory() {
        recentProjectTable.setItems(RecentProjectsController.getInstance().getRecentProjects());
        recentProjectTable.setRowFactory(new RecentProjectsRowFactory(this));
    }

    private void attachButtonsEvents() {
        buttonsBox.getCreateProjectButton().setOnAction(this::createProjectHandler);
        buttonsBox.getOpenProjectButton().setOnAction(this::openProjectHandler);
    }

    private void createProjectHandler(ActionEvent event) {
        CreateNewProjectWindowController newProjectWindowController = new CreateNewProjectWindowController();
        newProjectWindowController.launch();
    }

    private void openProjectHandler(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Documents/Cachoeira"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CACH", "*.cach"));
        File file = fileChooser.showOpenDialog(view.getStage());
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
            if (view.getStage().isShowing()) {
                view.getStage().close(); //закрываем стартовое окно
            }
            MainWindowController mainWindowController = new MainWindowController(project);
            mainWindowController.launch();
        }
    }

    public IView getView() {
        return view;
    }
}
