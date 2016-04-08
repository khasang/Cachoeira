package ru.khasang.cachoeira.view.mainwindow;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.ResourceType;
import ru.khasang.cachoeira.view.UIControl;

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
    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener nameFieldFocusListener;

    public ResourcePropertiesPaneController() {
    }

    public void initFields(UIControl uiControl) {
        // Заполняем выпадающий список
        ObservableList<ResourceType> resourceTypesModel = FXCollections.observableArrayList(ResourceType.values());
        resourceTypeComboBox.setItems(resourceTypesModel);
        // Делаем панель не активной, если ресурс не выбран
        propertiesPane.disableProperty().bind(uiControl.getController().selectedResourceProperty().isNull());

        /* Поле наименование */
        nameField.setOnKeyPressed(keyEvent -> {
            // Изменения применяем только при нажатии на ENTER...
            if (keyEvent.getCode() == KeyCode.ENTER) {
                // Если поле не пустое
                if (!nameField.getText().trim().isEmpty()) {
                    uiControl.getController().getSelectedResource().setName(nameField.getText());
                    // Убираем фокусировку с поля наименования задачи
                    nameField.getParent().requestFocus();
                }
            }
            // Если нажали ESC,
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                // то возвращаем предыдущее название
                nameField.setText(uiControl.getController().getSelectedResource().getName());
                // Убираем фокусировку с поля наименования задачи
                nameField.getParent().requestFocus();
            }
        });
        // ... или при потере фокуса.
        nameFieldFocusListener = observable -> {
            if (!nameField.isFocused()) {
                // Если поле не пустое, то
                if (!nameField.getText().trim().isEmpty()) {
                    // применяем изменения
                    uiControl.getController().getSelectedResource().setName(nameField.getText());
                } else {
                    // либо возвращаем предыдущее название
                    nameField.setText(uiControl.getController().getSelectedResource().getName());
                }
            }
        };
        nameField.focusedProperty().addListener(new WeakInvalidationListener(nameFieldFocusListener));
    }

    /**
     * Заполняем таблицу с привязанными задачами
     */
    public void initAssignmentTaskTable(UIControl uiControl) {
        taskTableView.setItems(uiControl.getController().getProject().getTaskList());
        taskNameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty());
    }

    public void setListeners(UIControl uiControl) {
        taskListListener = change -> initCheckBoxColumn(uiControl.getController().getSelectedResource());
        selectedResourceListener = (observable, oldSelectedResource, newSelectedResource) -> {
            // Прежде чем привязать поля свойств нового ресурса необходимо отвязать поля предыдущего ресурса
            // (если такой был)
            if (oldSelectedResource != null) {
//                nameField.textProperty().unbindBidirectional(oldSelectedResource.nameProperty());
                emailField.textProperty().unbindBidirectional(oldSelectedResource.emailProperty());
                resourceTypeComboBox.valueProperty().unbindBidirectional(oldSelectedResource.resourceTypeProperty());
                descriptionTextArea.textProperty().unbindBidirectional(oldSelectedResource.descriptionProperty());
            }
            // Привязываем поля свойств к модели
            if (newSelectedResource != null) {
//                nameField.textProperty().bindBidirectional(newSelectedResource.nameProperty());
                nameField.setText(newSelectedResource.getName());
                emailField.textProperty().bindBidirectional(newSelectedResource.emailProperty());
                resourceTypeComboBox.valueProperty().bindBidirectional(newSelectedResource.resourceTypeProperty());
                descriptionTextArea.textProperty().bindBidirectional(newSelectedResource.descriptionProperty());
            }
            // Если выбрали другой ресурс перерисовываем таблицу привязанных задач
            initCheckBoxColumn(uiControl.getController().getSelectedResource());
        };

        // TODO: 18.01.2016 Разобраться почему не работает с WeakChangeListener'ом, при том что абсолютно такая же хрень в TaskPropertiesPaneController работает как часы.
        uiControl.getController().selectedResourceProperty().addListener((selectedResourceListener));
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
                            currentRowTask.getResourceList()
                                    .stream()
                                    .filter(resource -> selectedResource.equals(resource) && !checkBox.isSelected())
                                    .forEach(resource -> checkBox.setSelected(true));
                        }
                    }
                }
            });
        }
    }
}
