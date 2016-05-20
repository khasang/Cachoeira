package ru.khasang.cachoeira.vcontroller;

import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import ru.khasang.cachoeira.data.DataService;
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
        CreateNewProjectWindowController newProjectWindowController = new CreateNewProjectWindowController(view);
        newProjectWindowController.launch();
    }

    private void openProjectHandler(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Documents/Cachoeira"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CACH", "*.cach"));
        File file = fileChooser.showOpenDialog(view.getStage());
        if (file != null) {
            DataService.getInstance().loadProject(file);
            view.getStage().close();
        }
    }

    public IView getView() {
        return view;
    }
}
