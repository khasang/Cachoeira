package ru.khasang.cachoeira.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.khasang.cachoeira.controller.IController;

import java.io.IOException;

/**
 * Created by truesik on 11.11.2015.
 */
public class NewProjectWindow implements IWindow {
    private final IController controller;
    private final UIControl UIControl;

    @FXML
    private TextField newProjectNameField;
    @FXML
    private TextField newProjectPathField;
    @FXML
    private DatePicker newProjectStartDatePicker;
    @FXML
    private DatePicker newProjectFinishDatePicker;
    @FXML
    private TextArea newProjectDescriptionArea;

    private Parent root = null;
    private Stage stage;

    public NewProjectWindow(IController controller, UIControl UIControl) {
        this.controller = controller;
        this.UIControl = UIControl;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NewProjectWindow.fxml"));    //грузим макет окна
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
        stage.show();
        stage.setTitle("New Project");
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @FXML
    private void newProjectPathChooserButtonHandle(ActionEvent actionEvent) {

    }

    @FXML
    private void newProjectCreateButtonHandle(ActionEvent actionEvent) {

    }

    @FXML
    private void newProjectCancelButtonHandle(ActionEvent actionEvent) {

    }
}
