package ru.khasang.cachoeira.view.mainwindow;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.commands.CommandControl;
import ru.khasang.cachoeira.commands.resource.RenameResourceCommand;
import ru.khasang.cachoeira.commands.resource.SetResourceDescriptionCommand;
import ru.khasang.cachoeira.commands.resource.SetResourceEmailCommand;
import ru.khasang.cachoeira.commands.resource.SetResourceTypeCommand;
import ru.khasang.cachoeira.commands.task.AddResourceToTaskCommand;
import ru.khasang.cachoeira.commands.task.RemoveResourceFromTaskCommand;
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
        resourceTypeComboBox.setItems(FXCollections.observableArrayList(ResourceType.values()));
        // Делаем панель не активной, если ресурс не выбран
        propertiesPane.disableProperty().bind(uiControl.getController().selectedResourceProperty().isNull());

        /* Поле наименование */
        nameField.setOnKeyPressed(keyEvent -> {
            // Изменения применяем только при нажатии на ENTER...
            if (keyEvent.getCode() == KeyCode.ENTER) {
                // Если поле не пустое
                if (!nameField.getText().trim().isEmpty()) {
                    CommandControl.getInstance().execute(new RenameResourceCommand(uiControl.getController().getSelectedResource(), nameField.getText()));
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
                nameField.setText(uiControl.getController().getSelectedResource().getName());
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
            // Привязываем поля свойств к модели
            if (newSelectedResource != null) {
                nameField.setText(newSelectedResource.getName());
                emailField.setText(newSelectedResource.getEmail());
                resourceTypeComboBox.setValue(newSelectedResource.getType());
                descriptionTextArea.setText(newSelectedResource.getDescription());

                uiControl.getController().getSelectedResource().nameProperty().addListener(observable1 -> {
                    nameField.setText(uiControl.getController().getSelectedResource().getName());
                });
                uiControl.getController().getSelectedResource().emailProperty().addListener(observable1 -> {
                    emailField.setText(uiControl.getController().getSelectedResource().getEmail());
                });
                uiControl.getController().getSelectedResource().resourceTypeProperty().addListener(observable1 -> {
                    resourceTypeComboBox.setValue(uiControl.getController().getSelectedResource().getType());
                });
                uiControl.getController().getSelectedResource().descriptionProperty().addListener(observable1 -> {
                    descriptionTextArea.setText(uiControl.getController().getSelectedResource().getDescription());
                });

                // TODO: 15.04.2016 Исправить текстовые проперти а-ля nameField
                emailField.textProperty().addListener((observable1, oldValue, newValue) -> {
                    CommandControl.getInstance().execute(new SetResourceEmailCommand(uiControl.getController().getSelectedResource(), newValue));
                });

                resourceTypeComboBox.valueProperty().addListener((observable1, oldValue, newValue) -> {
                    CommandControl.getInstance().execute(new SetResourceTypeCommand(uiControl.getController().getSelectedResource(), newValue));
                });

                descriptionTextArea.textProperty().addListener((observable1, oldValue, newValue) -> {
                    CommandControl.getInstance().execute(new SetResourceDescriptionCommand(uiControl.getController().getSelectedResource(), newValue));
                });
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
                                CommandControl.getInstance().execute(new AddResourceToTaskCommand(currentRowTask, selectedResource));
                            } else {
                                CommandControl.getInstance().execute(new RemoveResourceFromTaskCommand(currentRowTask, selectedResource));
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
