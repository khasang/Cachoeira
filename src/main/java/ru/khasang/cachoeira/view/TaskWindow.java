package ru.khasang.cachoeira.view;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.PriorityType;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by truesik on 28.09.2015.
 */
public class TaskWindow implements IWindow {
    @FXML
    private Button taskWindowOKButton;
    @FXML
    private TextField taskNameField;
    @FXML
    private TableView<IResource> resourceTableView;
    @FXML
    private TableColumn<IResource, String> resourceNameColumn;
    @FXML
    private TableColumn<IResource, Boolean> resourceCheckboxColumn;
    @FXML
    private DatePicker taskStartDatePicker;
    @FXML
    private DatePicker taskFinishDatePicker;
    //добавил:
    @FXML
    private TextField taskCostField;
    @FXML
    private Slider taskDonePercentSlider;
    @FXML
    private ComboBox<PriorityType> taskPriorityComboBox;

    private MainWindow mainWindow;
    private IController controller;
    private boolean isNewTask = false; //если тру, то нажата кнопка Новая задача, если фолз, то Свойства задачи
    private Parent root = null;
    private Stage stage;
    private ObservableList<PriorityType> taskPriorityTypes = FXCollections.observableArrayList();
    private ObservableList<IResource> resourceTableModel = FXCollections.observableArrayList();
    private ObservableList<IResource> resourceList;

    public TaskWindow(MainWindow mainWindow, IController controller, boolean isNewTask) {
        this.mainWindow = mainWindow;
        this.controller = controller;
        this.isNewTask = isNewTask;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/TaskWindow.fxml"));   //грузим макет окна
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

        stage.initOwner(mainWindow.getStage());     //todo исправить на viewController
        stage.initModality(Modality.WINDOW_MODAL);  //чтобы окно сделать модальным, ему нужно присвоить "владельца" (строчка выше)
        stage.setResizable(false);                  //размер окна нельзя изменить
        stage.show();
        taskPriorityTypes.addAll(PriorityType.values());
        taskPriorityComboBox.setItems(taskPriorityTypes);

        resourceTableModel.addAll(controller.getProject().getResourceList());
        resourceTableView.getItems().addAll(resourceTableModel);

        //отключает возможность в Дате окончания выбрать дату предыдущую Начальной даты
        taskFinishDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(taskStartDatePicker.getValue().plusDays(1))) {
                            setDisable(true);
                        }
                    }
                };
            }
        });

        if (isNewTask) {
            stage.setTitle("Новая задача");

            resourceList = FXCollections.observableArrayList(); //todo пришлось сделалть дополнительный список в который сбрасываются ресурсы с нажатым чекбоксом, т.к. я не понял как вытащить инфу из таблицы

            taskWindowOKButton.setDisable(true);        //отключаем клопку ОК, пока не будут заполнены/изменены поля
            taskStartDatePicker.setValue(LocalDate.now());                              //дефолтовое значение: Сегодняшняя дата
            taskFinishDatePicker.setValue(taskStartDatePicker.getValue().plusDays(1));  //плюс один день
            //taskDonePercent.setValue(0);
            taskCostField.setText(String.valueOf(0.00));
            taskDonePercentSlider.setValue(0);
            taskPriorityComboBox.setValue(PriorityType.Normal);

            resourceNameColumn.setCellValueFactory(param -> param.getValue().nameProperty());  //колонка с именами ресурсов
            resourceCheckboxColumn.setCellFactory(new Callback<TableColumn<IResource, Boolean>, TableCell<IResource, Boolean>>() {
                @Override
                public TableCell<IResource, Boolean> call(TableColumn<IResource, Boolean> param) {
                    return new TableCell<IResource, Boolean>() {
                        @Override
                        public void updateItem(Boolean item, boolean empty) {
                            super.updateItem(item, empty);
                            setAlignment(Pos.CENTER);
//                            checkBox.setAlignment(Pos.CENTER);
                            TableRow<IResource> currentRow = getTableRow();
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
                                            resourceList.add(currentRow.getItem());
                                        } else {
                                            resourceList.remove(currentRow.getItem());
                                        }
                                    }
                                });
                            }
                        }
                    };
                }
            });
        } else {
            stage.setTitle("Свойства задачи");

            resourceList = FXCollections.observableArrayList(controller.getSelectedTask().getResourceList()); //todo пришлось сделалть дополнительный список в который сбрасываются ресурсы с нажатым чекбоксом, т.к. я не понял как вытащить инфу из таблицы

            taskNameField.setText(controller.getSelectedTask().getName());
            taskStartDatePicker.setValue(controller.getSelectedTask().getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            taskFinishDatePicker.setValue(controller.getSelectedTask().getFinishDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            //добавляю:
            taskCostField.setText(String.valueOf(controller.getSelectedTask().getCost()));
            taskDonePercentSlider.setValue(controller.getSelectedTask().getDonePercent());

            taskPriorityComboBox.getSelectionModel().select(controller.getSelectedTask().getPriorityType());

            resourceNameColumn.setCellValueFactory(param -> param.getValue().nameProperty());
            resourceCheckboxColumn.setCellFactory(new Callback<TableColumn<IResource, Boolean>, TableCell<IResource, Boolean>>() {
                @Override
                public TableCell<IResource, Boolean> call(TableColumn<IResource, Boolean> param) {
                    return new TableCell<IResource, Boolean>() {
                        @Override
                        public void updateItem(Boolean item, boolean empty) {
                            super.updateItem(item, empty);
                            setAlignment(Pos.CENTER);
//                            checkBox.setAlignment(Pos.CENTER);
                            TableRow<IResource> currentRow = getTableRow();
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
                                            resourceList.add(currentRow.getItem());
                                        } else {
                                            resourceList.remove(currentRow.getItem());
                                        }
                                    }
                                });

                                for (IResource resource : controller.getSelectedTask().getResourceList()) {
                                    if (resource.equals(currentRow.getItem())) {
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

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                validate();
            }
        };

        taskNameField.textProperty().addListener(changeListener);
        taskStartDatePicker.valueProperty().addListener(changeListener);
        taskFinishDatePicker.valueProperty().addListener(changeListener);
        //добавил:
        taskCostField.textProperty().addListener(changeListener);
        taskDonePercentSlider.valueProperty().addListener(changeListener);
        taskPriorityComboBox.valueProperty().addListener(changeListener);
    }

    private void validate() {
        taskWindowOKButton.disableProperty().set(
                taskNameField.getText().trim().isEmpty() ||
                        taskCostField.getText().trim().isEmpty() ||
                        taskStartDatePicker.getValue().toString().trim().isEmpty() ||
                        taskFinishDatePicker.getValue().toString().trim().isEmpty()); // отключаем кнопку ОК, если хотя бы одно из полей не заполнено todo сделать проверку на null, иначе nullPointerException

    }


    @Override
    public Stage getStage() {
        return stage;
    }

    @FXML
    public void taskWindowOKButtonHandle(ActionEvent actionEvent) {
        //добавляем задачу и закрываем окошко
        Date taskStartDate = Date.from(taskStartDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());    //LocalDate to Date
        Date taskFinishDate = Date.from(taskFinishDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());  //LocalDate to Date

        //добавил:
        if (isNewTask) {
            controller.handleAddTask(taskNameField.getText(), taskStartDate, taskFinishDate, Double.valueOf(taskCostField.getText()), taskDonePercentSlider.getValue(), taskPriorityComboBox.getSelectionModel().getSelectedItem(), resourceList);
        } else {
            controller.handleChangeTask(taskNameField.getText(), taskStartDate, taskFinishDate, Double.valueOf(taskCostField.getText()), taskDonePercentSlider.getValue(), taskPriorityComboBox.getSelectionModel().getSelectedItem(), resourceList);
        }
        stage.close();
    }

    @FXML
    public void taskWindowCancelButtonHandle(ActionEvent actionEvent) {
        stage.close();
    }

    @FXML
    public void onlyNumber(KeyEvent event) {
        if ((isInteger(event.getText()) || event.getText().equals(".") && (countChar(taskCostField.getText(), ".") < 1)) || (event.getCode() == KeyCode.BACK_SPACE)) {
            taskCostField.setEditable(true);
            if ((taskCostField.getText().length() > 0) && (taskCostField.getText().lastIndexOf(".") != -1)) {
                if ((taskCostField.getText().length() > taskCostField.getText().lastIndexOf(".") + 2) && (event.getCode() != KeyCode.BACK_SPACE)) {
                    taskCostField.setEditable(false);
                }
            }
        } else {
            taskCostField.setEditable(false);
        }
    }

    private int countChar(String text, String s) {
        int count = 0;
        for (char element : text.toCharArray()) {
            if (element == '.') count++;
        }
        return count;
    }

    private boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
