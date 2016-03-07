package ru.khasang.cachoeira.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Класс-контроллер для StartWindow.fxml.
 */
public class StartWindow implements IWindow {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartWindow.class.getName());
    private UIControl uiControl;

    @FXML
    private TableView recentProjectsTableVIew;
    @FXML
    private TableColumn recentProjectNameColumn;
    @FXML
    private TableColumn recentProjectPathColumn;

    private Parent root = null;
    private Stage stage;

    public StartWindow(UIControl uiControl) {
        this.uiControl = uiControl;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/StartWindow.fxml"));    //грузим макет окна
        fxmlLoader.setResources(UIControl.BUNDLE);
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

    }
}
