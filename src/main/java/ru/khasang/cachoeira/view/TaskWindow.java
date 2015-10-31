package ru.khasang.cachoeira.view;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
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

    private MainWindow mainWindow;
    private IController controller;
    private boolean isNewTask = false; //если тру, то нажата кнопка Новая задача, если фолз, то Свойства задачи
    private Parent root = null;
    private Stage stage;
    private ObservableList<IResource> resourceTableModel = FXCollections.observableArrayList();
    private List<IResource> resourceList;

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
        stage.setTitle("Новая задача");
        stage.initOwner(mainWindow.getStage());     //todo исправить на viewController
        stage.initModality(Modality.WINDOW_MODAL);  //чтобы окно сделать модальным, ему нужно присвоить "владельца" (строчка выше)
        stage.setResizable(false);                  //размер окна нельзя изменить
        stage.show();

        resourceTableModel.addAll(controller.getProject().getResourceList());
        resourceTableView.getItems().addAll(resourceTableModel);
        resourceTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        resourceList = new ArrayList<>(); //todo пришлось сделалть дополнительный список в который сбрасываются ресурсы с нажатым чекбоксом, т.к. я не понял как вытащить инфу из таблицы


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
            taskWindowOKButton.setDisable(true);        //отключаем клопку ОК, пока не будут заполнены/изменены поля
            taskStartDatePicker.setValue(LocalDate.now());                              //дефолтовое значение: Сегодняшняя дата
            taskFinishDatePicker.setValue(taskStartDatePicker.getValue().plusDays(1));  //плюс один день

            resourceNameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getName()));  //колонка с именами ресурсов
            resourceCheckboxColumn.setCellFactory(new Callback<TableColumn<IResource, Boolean>, TableCell<IResource, Boolean>>() { //колонка с чекбоксами
                @Override
                public TableCell<IResource, Boolean> call(TableColumn<IResource, Boolean> param) {
                    return new TableCell<IResource, Boolean>() {
                        @Override
                        public void updateItem(Boolean item, boolean empty) {
                            super.updateItem(item, empty);
                            TableRow<IResource> currentRow = getTableRow();
                            setAlignment(Pos.CENTER);
                            if (empty) {
                                setText(null);
                                setGraphic(null);
                            } else {
                                CheckBox checkBox = new CheckBox();
                                checkBox.setAlignment(Pos.CENTER);
                                setGraphic(checkBox);
                                checkBox.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        if (checkBox.isSelected()) {                    //если нажали на чекбокс
                                            resourceList.add(currentRow.getItem());     //вытаскиваем строку и закидываем ее в список ресурсов, который потом при нажатии на ОК к задаче
                                        } else {
                                            resourceList.remove(currentRow.getItem());  //если убрали галку, то удаляем этот ресурс из списка
                                        }
                                    }
                                });
                            }
                        }
                    };
                }
            });

//            resourceCheckboxColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<IResource, Boolean>, ObservableValue<Boolean>() {
//                @Override
//                public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<IResource, Boolean> param) {
//                    CheckBox checkBox = new CheckBox();
//                    checkBox.setOnAction(new EventHandler<ActionEvent>() {
//                        @Override
//                        public void handle(ActionEvent event) {
//                            if (checkBox.isSelected()) {
//                                resourceList.add(param.getValue());
//                            } else {
//                                resourceList.remove(param.getValue());
//                            }
//                        }
//                    });
//                    return new SimpleObjectProperty<Boolean>(checkBox);
//                }
//            });
        } else {
            taskNameField.setText(controller.getSelectedTask().getName());
            taskStartDatePicker.setValue(controller.getSelectedTask().getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            taskFinishDatePicker.setValue(controller.getSelectedTask().getFinishDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            resourceNameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getName()));
            resourceCheckboxColumn.setCellFactory(new Callback<TableColumn<IResource, Boolean>, TableCell<IResource, Boolean>>() {
                @Override
                public TableCell<IResource, Boolean> call(TableColumn<IResource, Boolean> param) {
                    return new TableCell<IResource, Boolean>() {
                        @Override
                        public void updateItem(Boolean item, boolean empty) {
                            super.updateItem(item, empty);
                            setAlignment(Pos.CENTER);
//                            checkBox.setAlignment(Pos.CENTER);
                            if (empty) {
                                setText(null);
                                setGraphic(null);
                            } else {
                                CheckBox checkBox = new CheckBox();
                                for (IResource resource : param.getTableView().getItems()) {
                                    for (IResource iResource : controller.getSelectedTask().getResourceList()) {
                                        if (resource.equals(iResource)) {
                                            checkBox.setSelected(true);
                                        } else {
                                            checkBox.setSelected(false);
                                        }
                                    }
                                }
                                setGraphic(checkBox);
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
    }

    private void validate() {
        taskWindowOKButton.disableProperty().set(
                taskNameField.getText().trim().isEmpty() ||
                        taskStartDatePicker.getValue().toString().trim().isEmpty() ||
                        taskFinishDatePicker.getValue().toString().trim().isEmpty()); // отключаем кнопку ОК, если хотя бы одно из полей не заполнено todo сделать проверку на null, иначе nullPointerException
    }


    @Override
    public Stage getStage() {
        return stage;
    }

    public void taskWindowOKButtonHandle(ActionEvent actionEvent) {
        //добавляем задачу и закрываем окошко

        //преобразование из LocalDate в Date
//        LocalDate startLocalDate = taskStartDatePicker.getValue();
//        ZonedDateTime startZonedDateTime = startLocalDate.atStartOfDay(ZoneId.systemDefault());
//        Instant instant = Instant.from(startZonedDateTime);
//        Date taskStartDate = Date.from(instant);
//
//        LocalDate finishLocalDate = taskFinishDatePicker.getValue();
//        ZonedDateTime finishZonedDateTime = finishLocalDate.atStartOfDay(ZoneId.systemDefault());
//        Instant instant1 = Instant.from(finishZonedDateTime);
//        Date taskFinishDate = Date.from(instant1);

//        Date taskStartDate = Date.from(Instant.from(taskStartDatePicker.getValue().atStartOfDay(ZoneId.systemDefault())));      //LocalDate to Date
//        Date taskFinishDate = Date.from(Instant.from(taskFinishDatePicker.getValue().atStartOfDay(ZoneId.systemDefault())));    //LocalDate to Date

        Date taskStartDate = Date.from(taskStartDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());    //LocalDate to Date
        Date taskFinishDate = Date.from(taskFinishDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());  //LocalDate to Date

        if (isNewTask) {
            controller.handleAddTask(taskNameField.getText(), taskStartDate, taskFinishDate, resourceList);

        } else {
            controller.handleChangeTask(taskNameField.getText(), taskStartDate, taskFinishDate);
//            taskController.getSelectedTask().setName(taskNameField.getText());
//            taskController.getSelectedTask().setStartDate(taskStartDate);
//            taskController.getSelectedTask().setFinishDate(taskFinishDate);
        }
        mainWindow.refreshTaskTableModel();
        stage.close();
    }

    public void taskWindowCancelButtonHandle(ActionEvent actionEvent) {
        stage.close();
    }
}
