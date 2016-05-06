package ru.khasang.cachoeira.viewcontroller.startwindow;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.data.DBSchemeManager;
import ru.khasang.cachoeira.data.DataStoreInterface;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.properties.RecentProjectsController;
import ru.khasang.cachoeira.viewcontroller.IWindow;
import ru.khasang.cachoeira.viewcontroller.UIControl;

import java.io.File;
import java.io.IOException;

/**
 * Класс-контроллер для StartWindow.fxml.
 */
public class StartWindow implements IWindow {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartWindow.class.getName());
    private UIControl uiControl;

    @FXML
    private TableView<File> recentProjectsTableVIew;
    @FXML
    private TableColumn<File, String> recentProjectPathColumn;

    private Parent root = null;
    private Stage stage;
    private RecentProjectsController recentProjectsController = RecentProjectsController.getInstance();

    public StartWindow(UIControl uiControl) {
        this.uiControl = uiControl;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/StartWindow.fxml"));    //грузим макет окна
        fxmlLoader.setResources(UIControl.bundle);
        fxmlLoader.setController(this);                                                             //говорим макету, что этот класс является его контроллером
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void launch() {
        stage = new Stage();
        if (root != null) {
            stage.setScene(new Scene(root));
        }
        stage.setResizable(false);
        stage.show();
        stage.setTitle("Cachoeira");
        LOGGER.debug("Открыто стартовое окно.");

        recentProjectsTableVIew.setItems(recentProjectsController.getRecentProjects());
        recentProjectsTableVIew.setRowFactory(new RecentProjectsRowFactory(uiControl));
        recentProjectPathColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPath()));
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @FXML
    private void openNewProjectWindowHandle(ActionEvent actionEvent) {
        LOGGER.debug("Нажата кнопка создания нового проекта.");
        uiControl.launchNewProjectWindow();
    }

    @FXML
    private void openProjectFileChooserHandle(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Documents/Cachoeira"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CACH", "*.cach"));
        uiControl.setFile(fileChooser.showOpenDialog(this.stage));
        if (uiControl.getFile() != null) {
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

            recentProjectsController.addRecentProject(uiControl.getFile());
            if (uiControl.getStartWindow().getStage().isShowing()) {
                uiControl.getStartWindow().getStage().close(); //закрываем стартовое окно
            }
            uiControl.launchMainWindow(); //запускаем главное окно
        }
    }
}
