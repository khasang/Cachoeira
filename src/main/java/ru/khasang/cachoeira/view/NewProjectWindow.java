package ru.khasang.cachoeira.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.khasang.cachoeira.controller.IController;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by truesik on 11.11.2015.
 */
public class NewProjectWindow implements IWindow {
    private IController controller;
    private UIControl UIControl;

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
        stage = new Stage(StageStyle.UTILITY);
        if (root != null) {
            stage.setScene(new Scene(root));
        }
        stage.setResizable(false);
        stage.initOwner(UIControl.getStartWindow().getStage());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
        stage.setTitle("Новый проект");


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
        Date projectStartDate = Date.from(newProjectStartDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());    //LocalDate to Date
        Date projectFinishDate = Date.from(newProjectFinishDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        controller.notifyAddProject(newProjectNameField.getText(), projectStartDate, projectFinishDate, newProjectDescriptionArea.getText());
        stage.close();
        UIControl.getStartWindow().getStage().close();
        UIControl.launchMainWindow();
    }

    @FXML
    private void newProjectCancelButtonHandle(ActionEvent actionEvent) {
        stage.close();
    }
}
