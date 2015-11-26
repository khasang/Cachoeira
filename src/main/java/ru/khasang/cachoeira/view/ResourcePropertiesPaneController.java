package ru.khasang.cachoeira.view;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.ResourceType;

/**
 * Created by truesik on 24.11.2015.
 */
public class ResourcePropertiesPaneController {
    //Информация
    @FXML
    private VBox propertiesPane;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private ComboBox<ResourceType> resourceTypeComboBox;

    //Привязанные задачи
    @FXML
    private TableView<ITask> taskTableView;
    @FXML
    private TableColumn<ITask, String> taskNameColumn;
    @FXML
    private TableColumn<ITask, Boolean> taskCheckboxColumn;

    private IController controller;

    public ResourcePropertiesPaneController() {
    }

    public void setController(IController controller) {
        this.controller = controller;
    }

    public void initFields() {
        ObservableList<ResourceType> resourceTypesModel = FXCollections.observableArrayList(ResourceType.values());
        resourceTypeComboBox.setItems(resourceTypesModel);

        /** Делаем панель не активной, если ресурс не выбран **/
        propertiesPane.disableProperty().bind(controller.selectedResourceProperty().isNull());

        controller.selectedResourceProperty().addListener((observable, oldValue, newValue) -> {
            /** Прежде чем привязать поля свойств нового ресурса необходимо отвязать поля предыдущего ресурса (если такой был) **/
            if (oldValue != null) {
                nameField.textProperty().unbindBidirectional(oldValue.nameProperty());
                emailField.textProperty().unbindBidirectional(oldValue.emailProperty());
                resourceTypeComboBox.valueProperty().unbindBidirectional(oldValue.resourceTypeProperty());
            }
            /** Привязываем поля свойств к модели **/
            if (newValue != null) {
                nameField.textProperty().bindBidirectional(newValue.nameProperty());
                emailField.textProperty().bindBidirectional(newValue.emailProperty());
                resourceTypeComboBox.valueProperty().bindBidirectional(newValue.resourceTypeProperty());
            }
        });
    }

    public void initTaskTable() {
        taskTableView.getItems().addAll(controller.getProject().getTaskList());
        taskNameColumn.setCellValueFactory(param -> param.getValue().nameProperty());
        taskCheckboxColumn.setCellFactory(new Callback<TableColumn<ITask, Boolean>, TableCell<ITask, Boolean>>() {
            @Override
            public TableCell<ITask, Boolean> call(TableColumn<ITask, Boolean> param) {
                return new TableCell<ITask, Boolean>() {
                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        setAlignment(Pos.CENTER);
                        TableRow<ITask> currentRow = getTableRow();
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            /** Заполняем столбец чек-боксами **/
                            CheckBox checkBox = new CheckBox();
                            setGraphic(checkBox);
                            checkBox.setOnAction(event -> {
                                if (checkBox.isSelected()) {
                                    currentRow.getItem().addResource(controller.getSelectedResource());
                                } else {
                                    currentRow.getItem().removeResource(controller.getSelectedResource());
                                }
                            });

                            /** Расставляем галочки на нужных строках **/
                            for (IResource resource : currentRow.getItem().getResourceList()) {
                                if (controller.selectedResourceProperty().getValue().equals(resource)) {
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
