package ru.khasang.cachoeira.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by truesik on 28.09.2015.
 */
public class TaskWindow {


    private final Stage mainWindow;
    @FXML
    private TextField taskNameField;
    @FXML
    private TableView resourceTableView;
    @FXML
    private TableColumn resourceNameColumn;
    @FXML
    private TableColumn resourceCheckboxColumn;
    @FXML
    private DatePicker taskStartDatePicker;
    @FXML
    private DatePicker taskFinishDatePicker;

    private Parent root = null;
    private Stage stage;

    public TaskWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/TaskWindow.fxml"));   //грузим макет окна
        fxmlLoader.setController(this);                                                         //говорим макету, что этот класс является его контроллером
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launch() {
        stage = new Stage(StageStyle.UTILITY);      //StageStyle.UTILITY - в тайтле только один крестик
        if (root != null) {
            stage.setScene(new Scene(root));
        }
        stage.setTitle("Новая задача");
        stage.initOwner(mainWindow);
        stage.initModality(Modality.WINDOW_MODAL);  //чтобы окно сделать модальным, ему нужно присвоить "владельца" (строчка выше)
        stage.setResizable(false);                  //размер окна нельзя изменить
        stage.show();
    }

    public void taskWindowOKButtonHandle(ActionEvent actionEvent) {
        //добавляем задачу и закрываем окошко
        stage.close();
    }

    public void taskWindowCancelButtonHandle(ActionEvent actionEvent) {
        stage.close();
    }
}
