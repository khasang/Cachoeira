package ru.khasang.cachoeira.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
public class ResourceWindow {

    @FXML
    private TextField resourceNameField;
    @FXML
    private TableView taskTableView;
    @FXML
    private TableColumn taskNameColumn;
    @FXML
    private TableColumn taskCheckboxColumn;
    @FXML
    private TextField resourceTypeField;

    private Parent root = null;
    private Stage stage;

    public ResourceWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ResourceWindow.fxml"));  //грузим макет окна
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
        stage.setTitle("Новый ресурс");
//        stage.initOwner(nameOfParentWindow);
        stage.initModality(Modality.WINDOW_MODAL);  //чтобы окно сделать модальным, ему нужно присвоить "владельца" (строчка выше)
        stage.setResizable(false);                  //размер окна нельзя изменить
        stage.show();
    }

    public void resourceWindowOKButtonHandle(ActionEvent actionEvent) {
        //добавляем ресурс и закрываем окошко
        stage.close();
    }

    public void resourceWindowCancelButtonHandle(ActionEvent actionEvent) {
        stage.close();
    }
}
