package ru.khasang.cachoeira.view;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.ResourceType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    private TableView<ITask> taskTableView;
    @FXML
    private TableColumn<ITask, String> taskNameColumn;
    @FXML
    private TableColumn<ITask, Boolean> taskCheckboxColumn;

    private Parent root = null;
    private Stage stage;
    private MainWindow mainWindow;
    private IController controller;
    private boolean isNewResource = false; //если тру, то значит нажали кнопку Новый ресурс, если фолз, то Свойства ресурса
    private ObservableList<ResourceType> resourceTypesModel = FXCollections.observableArrayList();
    private ObservableList<ITask> taskModel = FXCollections.observableArrayList();
    private List<ITask> taskList;

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
        stage.initOwner(mainWindow.getStage());
        stage.initModality(Modality.WINDOW_MODAL);  //чтобы окно сделать модальным, ему нужно присвоить "владельца" (строчка выше)
        stage.setResizable(false);                  //размер окна нельзя изменить
        stage.show();

        //заполняем комбобокс типами ресурсов
        resourceTypesModel.addAll(ResourceType.values());
        resourceTypeComboBox.setItems(resourceTypesModel);

        taskModel.addAll(controller.getProject().getTaskList());
        taskTableView.getItems().addAll(taskModel);

        if (isNewResource) {
            stage.setTitle("Новый ресурс");
            taskList = new ArrayList<>();

            taskNameColumn.setCellValueFactory(param -> param.getValue().nameProperty());
            taskCheckboxColumn.setCellFactory(new Callback<TableColumn<ITask, Boolean>, TableCell<ITask, Boolean>>() {
                @Override
                public TableCell<ITask, Boolean> call(TableColumn<ITask, Boolean> param) {
                    return new TableCell<ITask, Boolean>() {
                        @Override
                        public void updateItem(Boolean item, boolean empty) {
                            super.updateItem(item, empty);
                            setAlignment(Pos.CENTER);
//                            checkBox.setAlignment(Pos.CENTER);
                            TableRow<ITask> currentRow = getTableRow();
                            if (empty) {
                                setText(null);
                                setGraphic(null);
                            } else {
                                CheckBox checkBox = new CheckBox();
                                setGraphic(checkBox);
                                checkBox.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        if (checkBox.isSelected()) {
                                            taskList.add(currentRow.getItem());
                                        } else {
                                            taskList.remove(currentRow.getItem());
                                        }
                                    }
                                });
                            }
                        }
                    };
                }
            });
        } else { //если свойства, то берем нужный ресурс и заполняем поля
            stage.setTitle("Свойства ресурса");
            resourceNameField.setText(controller.getSelectedResource().getName()); //имя
            resourceEmailField.setText(controller.getSelectedResource().getEmail()); //почта
            resourceTypeComboBox.getSelectionModel().select(controller.getSelectedResource().getType()); //тип ресурса

            taskList = new ArrayList<>();

            for (ITask task : controller.getProject().getTaskList()) {
                for (IResource resource : task.getResourceList()) {
                    if (controller.getSelectedResource().equals(resource)) {
                        taskList.add(task);
                    }
                }
            }
            taskNameColumn.setCellValueFactory(param -> param.getValue().nameProperty());
            taskCheckboxColumn.setCellFactory(new Callback<TableColumn<ITask, Boolean>, TableCell<ITask, Boolean>>() {
                @Override
                public TableCell<ITask, Boolean> call(TableColumn<ITask, Boolean> param) {
                    return new TableCell<ITask, Boolean>() {
                        @Override
                        public void updateItem(Boolean item, boolean empty) {
                            super.updateItem(item, empty);
                            setAlignment(Pos.CENTER);
//                            checkBox.setAlignment(Pos.CENTER);
                            TableRow<ITask> currentRow = getTableRow();
                            if (empty) {
                                setText(null);
                                setGraphic(null);
                            } else {
                                CheckBox checkBox = new CheckBox();
                                setGraphic(checkBox);
                                checkBox.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        if (checkBox.isSelected()) {
                                            taskList.add(currentRow.getItem());
                                        } else {
                                            taskList.remove(currentRow.getItem());
                                        }
                                    }
                                });
                                for (IResource resource : currentRow.getItem().getResourceList()) {
                                    if(controller.getSelectedResource().equals(resource)) {
                                        checkBox.selectedProperty().setValue(Boolean.TRUE);
                                        break;
                                    } else {
                                        checkBox.selectedProperty().setValue(Boolean.FALSE);
                                    }
                                }
                            }
                        }
                    };
                }
            });
        }
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @FXML
    public void resourceWindowOKButtonHandle(ActionEvent actionEvent) {
        //добавляем ресурс и закрываем окошко
        if (isNewResource) {
            controller.handleAddResource(resourceNameField.getText(), resourceEmailField.getText(), resourceTypeComboBox.getSelectionModel().getSelectedItem(), taskList);
        } else {
            controller.handleChangeResource(resourceNameField.getText(), resourceEmailField.getText(), resourceTypeComboBox.getSelectionModel().getSelectedItem(), taskList);
        }
        stage.close();
    }

    @FXML
    public void resourceWindowCancelButtonHandle(ActionEvent actionEvent) {
        stage.close();
    }
}
