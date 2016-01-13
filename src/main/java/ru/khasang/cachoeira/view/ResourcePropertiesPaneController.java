package ru.khasang.cachoeira.view;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.ResourceType;

/**
 * Класс-контроллер для ResourcePropertiesPane.fxml
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
    @FXML
    private TextArea descriptionTextArea;

    //Привязанные задачи
    @FXML
    private TableView<ITask> taskTableView;
    @FXML
    private TableColumn<ITask, String> taskNameColumn;
    @FXML
    private TableColumn<ITask, Boolean> taskCheckboxColumn;

    public ResourcePropertiesPaneController() {
    }

    public void initFields(UIControl uiControl) {
        // Заполняем выпадающий список
        ObservableList<ResourceType> resourceTypesModel = FXCollections.observableArrayList(ResourceType.values());
        resourceTypeComboBox.setItems(resourceTypesModel);

        // Делаем панель не активной, если ресурс не выбран
        propertiesPane.disableProperty().bind(uiControl.getController().selectedResourceProperty().isNull());

        uiControl.getController().selectedResourceProperty().addListener((observable, oldValue, newValue) -> {
            // Прежде чем привязать поля свойств нового ресурса необходимо отвязать поля предыдущего ресурса
            // (если такой был)
            if (oldValue != null) {
                nameField.textProperty().unbindBidirectional(oldValue.nameProperty());
                emailField.textProperty().unbindBidirectional(oldValue.emailProperty());
                resourceTypeComboBox.valueProperty().unbindBidirectional(oldValue.resourceTypeProperty());
                descriptionTextArea.textProperty().unbindBidirectional(oldValue.descriptionProperty());
            }
            // Привязываем поля свойств к модели
            if (newValue != null) {
                nameField.textProperty().bindBidirectional(newValue.nameProperty());
                emailField.textProperty().bindBidirectional(newValue.emailProperty());
                resourceTypeComboBox.valueProperty().bindBidirectional(newValue.resourceTypeProperty());
                descriptionTextArea.textProperty().bindBidirectional(newValue.descriptionProperty());
            }
        });
    }

    /**
     * Заполняем таблицу с привязанными задачами
     */
    public void initAssignmentTaskTable(UIControl uiControl) {
        taskTableView.setItems(uiControl.getController().getProject().getTaskList());
        taskNameColumn.setCellValueFactory(param -> param.getValue().nameProperty());
        uiControl.getController().selectedResourceProperty().addListener((observable, oldValue, newValue) -> initCheckBoxColumn(uiControl.getController().getSelectedResource()));
        // Не понимаю, что тут происходит, но работает
        uiControl.getController().getProject().getTaskList().addListener((ListChangeListener<ITask>) c -> initCheckBoxColumn(uiControl.getController().getSelectedResource()));
    }

    public void initCheckBoxColumn(IResource selectedResource) {
        taskCheckboxColumn.setCellFactory(new Callback<TableColumn<ITask, Boolean>, TableCell<ITask, Boolean>>() {
            @Override
            public TableCell<ITask, Boolean> call(TableColumn<ITask, Boolean> param) {
                return new TableCell<ITask, Boolean>() {
                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        setAlignment(Pos.CENTER);
                        ITask currentRowTask = (ITask) getTableRow().getItem();
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            // Заполняем столбец чек-боксами
                            CheckBox checkBox = new CheckBox();
                            setGraphic(checkBox);
                            checkBox.setOnAction(event -> {
                                if (checkBox.isSelected()) {
                                    currentRowTask.addResource(selectedResource);
                                } else {
                                    currentRowTask.removeResource(selectedResource);
                                }
                            });

                            // Расставляем галочки на нужных строках
                            if (currentRowTask != null) {
                                for (IResource resource : currentRowTask.getResourceList()) {
                                    if (selectedResource.equals(resource)) {
                                        checkBox.setSelected(true);
                                        break;
                                    } else {
                                        checkBox.setSelected(false);
                                    }
                                }
                            }
                        }
                    }
                };
            }
        });
    }
}
