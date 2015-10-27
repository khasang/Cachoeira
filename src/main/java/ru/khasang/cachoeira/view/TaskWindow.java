package ru.khasang.cachoeira.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import ru.khasang.cachoeira.controller.IController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by truesik on 28.09.2015.
 */
public class TaskWindow implements IWindow {
    @FXML
    private Button taskWindowOKButton;
    @FXML
    private TextField taskNameField;
    @FXML
    private TableView resourceTableView;
    @FXML
    private TableColumn resourceNameColumn;
    @FXML
    private TableColumn resourceCheckboxColumn;
    @FXML
    private DatePicker taskStartDatePicker;
    @FXML
    private DatePicker taskFinishDatePicker;

    private MainWindow mainWindow;
    private IController controller;
    private boolean isNewTask = false;
    private Parent root = null;
    private Stage stage;

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

        taskWindowOKButton.setDisable(true);        //отключаем клопку ОК, пока не будут заполнены/изменены поля

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
            taskStartDatePicker.setValue(LocalDate.now());                              //дефолтовое значение: Сегодняшняя дата
            taskFinishDatePicker.setValue(taskStartDatePicker.getValue().plusDays(1));  //плюс один день
        } else {
            taskNameField.setText(controller.getSelectedTask().getName());
            taskStartDatePicker.setValue(controller.getSelectedTask().getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            taskFinishDatePicker.setValue(controller.getSelectedTask().getFinishDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
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
            controller.handleAddTask(taskNameField.getText(), taskStartDate, taskFinishDate);
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
