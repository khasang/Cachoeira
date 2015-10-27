package ru.khasang.cachoeira.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.ResourceType;

import java.io.IOException;

/**
 * Created by truesik on 28.09.2015.
 */
public class ResourceWindow implements IWindow {
    @FXML
    private TextField resourceEmailField;
    @FXML
    private ComboBox<ResourceType> resourceTypeComboBox;
    @FXML
    private TextField resourceNameField;
    @FXML
    private TableView taskTableView;
    @FXML
    private TableColumn taskNameColumn;
    @FXML
    private TableColumn taskCheckboxColumn;

    private Parent root = null;
    private Stage stage;
    private MainWindow mainWindow;
    private IController controller;
    private boolean isNewResource = false; //если тру, то значит нажали кнопку Новый ресурс, если фолз, то Свойства ресурса
    private ObservableList<ResourceType> resourceTypesModel = FXCollections.observableArrayList();

    public ResourceWindow(MainWindow mainWindow, IController controller, boolean IsNewResource) {
        this.mainWindow = mainWindow;
        this.controller = controller;
        this.isNewResource = IsNewResource;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ResourceWindow.fxml"));  //грузим макет окна
        fxmlLoader.setController(this);                                                         //говорим макету, что этот класс является его контроллером
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void launch() {
        stage = new Stage(StageStyle.UTILITY);      //StageStyle.UTILITY - в тайтле только один крестик
        if (root != null) {
            stage.setScene(new Scene(root));
        }
        stage.setTitle("Новый ресурс");
        stage.initOwner(mainWindow.getStage());
        stage.initModality(Modality.WINDOW_MODAL);  //чтобы окно сделать модальным, ему нужно присвоить "владельца" (строчка выше)
        stage.setResizable(false);                  //размер окна нельзя изменить
        stage.show();

        //заполняем комбобокс типами ресурсов
        resourceTypesModel.addAll(ResourceType.values());
        resourceTypeComboBox.setItems(resourceTypesModel);

        if (!isNewResource) { //если свойства, то берем нужный ресурс и заполняем поля
            resourceNameField.setText(controller.getSelectedResource().getName()); //имя
            resourceEmailField.setText(controller.getSelectedResource().getEmail()); //почта
            resourceTypeComboBox.getSelectionModel().select(controller.getSelectedResource().getType()); //тип ресурса
        }
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    public void resourceWindowOKButtonHandle(ActionEvent actionEvent) {
        //добавляем ресурс и закрываем окошко
        if (isNewResource) {
            controller.handleAddResource(resourceNameField.getText(), resourceEmailField.getText(), resourceTypeComboBox.getSelectionModel().getSelectedItem());
        } else {
            controller.handleChangeResource(resourceNameField.getText(), resourceEmailField.getText(), resourceTypeComboBox.getSelectionModel().getSelectedItem());
        }
        mainWindow.refreshResourceTableModel();
        stage.close();
    }

    public void resourceWindowCancelButtonHandle(ActionEvent actionEvent) {
        stage.close();
    }
}
