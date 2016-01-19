package ru.khasang.cachoeira.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.ResourceType;

/**
 * Класс-контроллер для ResourcePropertiesPane.fxml
 */
public class ResourcePropertiesPaneController {
    // Информация
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

    // Привязанные задачи
    @FXML
    private TableView<ITask> taskTableView;
    @FXML
    private TableColumn<ITask, String> taskNameColumn;
    @FXML
    private TableColumn<ITask, Boolean> taskCheckboxColumn;

    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<IResource> selectedResourceListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<ITask> taskListListener;

    public ResourcePropertiesPaneController() {
    }

    public void initFields(UIControl uiControl) {
        // Заполняем выпадающий список
        ObservableList<ResourceType> resourceTypesModel = FXCollections.observableArrayList(ResourceType.values());
        resourceTypeComboBox.setItems(resourceTypesModel);
        // Делаем панель не активной, если ресурс не выбран
        propertiesPane.disableProperty().bind(uiControl.getController().selectedResourceProperty().isNull());
    }

    /**
     * Заполняем таблицу с привязанными задачами
     */
    public void initAssignmentTaskTable(UIControl uiControl) {
        taskTableView.setItems(uiControl.getController().getProject().getTaskList());
        taskNameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty());
    }

    public void setListeners(UIControl uiControl) {
        // TODO: 18.01.2016 Заставить эту хрень работать
        taskListListener = change -> initCheckBoxColumn(uiControl.getController().getSelectedResource());
        selectedResourceListener = (observable, oldSelectedResource, newSelectedResource) -> {
            // Прежде чем привязать поля свойств нового ресурса необходимо отвязать поля предыдущего ресурса
            // (если такой был)
            System.out.println("yes");
            if (oldSelectedResource != null) {
                nameField.textProperty().unbindBidirectional(oldSelectedResource.nameProperty());
                emailField.textProperty().unbindBidirectional(oldSelectedResource.emailProperty());
                resourceTypeComboBox.valueProperty().unbindBidirectional(oldSelectedResource.resourceTypeProperty());
                descriptionTextArea.textProperty().unbindBidirectional(oldSelectedResource.descriptionProperty());
            }
            // Привязываем поля свойств к модели
            if (newSelectedResource != null) {
                nameField.textProperty().bindBidirectional(newSelectedResource.nameProperty());
                emailField.textProperty().bindBidirectional(newSelectedResource.emailProperty());
                resourceTypeComboBox.valueProperty().bindBidirectional(newSelectedResource.resourceTypeProperty());
                descriptionTextArea.textProperty().bindBidirectional(newSelectedResource.descriptionProperty());
            }
            // Если выбрали другой ресурс перерисовываем таблицу привязанных задач
            initCheckBoxColumn(uiControl.getController().getSelectedResource());
        };

        uiControl.getController().selectedResourceProperty().addListener(new WeakChangeListener<>(selectedResourceListener));
        // Если обновляется список задач, то обновляем таблицу
        uiControl.getController().getProject().getTaskList().addListener(new WeakListChangeListener<>(taskListListener));
    }

    public void initCheckBoxColumn(IResource selectedResource) {
        if (selectedResource != null) {
            taskCheckboxColumn.setCellFactory(column -> new TableCell<ITask, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
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
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}
