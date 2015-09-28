package ru.khasang.cachoeira.view;

import javafx.event.ActionEvent;
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

    public TextField taskNameField;
    public TableView resourceTableView;
    public TableColumn resourceNameColumn;
    public TableColumn resourceCheckboxColumn;
    public DatePicker taskStartDatePicker;
    public DatePicker taskFinishDatePicker;
    Parent root = null;
    Stage stage;

    public TaskWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TaskWindow.fxml"));   //грузим макет окна
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
//        stage.initOwner(nameOfParentWindow);
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
