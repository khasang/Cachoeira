package ru.khasang.cachoeira.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import ru.khasang.cachoeira.controller.IController;

import java.io.IOException;

/**
 * Created by truesik on 11.11.2015.
 */
public class StartWindow implements IWindow {
    private IController controller;
    private UIControl UIControl;

    @FXML
    private TableView recentProjectsTableVIew;
    @FXML
    private TableColumn recentProjectNameColumn;
    @FXML
    private TableColumn recentProjectPathColumn;

    private Parent root = null;
    private Stage stage;

    public StartWindow(IController controller, UIControl UIControl) {
        this.controller = controller;
        this.UIControl = UIControl;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/StartWindow.fxml"));    //грузим макет окна
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
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @FXML
    private void openNewProjectWindowHandle(ActionEvent actionEvent) {
        UIControl.launchNewProjectWindow();
    }

    @FXML
    private void openProjectFileChooserHandle(ActionEvent actionEvent) {

    }
}
