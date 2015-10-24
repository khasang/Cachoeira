package ru.khasang.cachoeira.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.khasang.cachoeira.controller.TaskController;
import ru.khasang.cachoeira.model.ITask;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created by truesik on 28.09.2015.
 */
public class TaskWindow implements IWindow {
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
    private boolean isNewTask = false;
    private Parent root = null;
    private Stage stage;
    private TaskController taskController;

    public TaskWindow(MainWindow mainWindow, TaskController taskController, boolean isNewTask) {
        this.mainWindow = mainWindow;
        this.taskController = taskController;
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

        if (!isNewTask) {
            taskNameField.setText(taskController.getSelectedTask().getName());
            taskStartDatePicker.setValue(taskController.getSelectedTask().getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            taskFinishDatePicker.setValue(taskController.getSelectedTask().getFinishDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
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
            taskController.addTask(taskNameField.getText(), taskStartDate, taskFinishDate);
        } else {
            taskController.updateTask(taskNameField.getText(), taskStartDate, taskFinishDate);
//            taskController.getSelectedTask().setName(taskNameField.getText());
//            taskController.getSelectedTask().setStartDate(taskStartDate);
//            taskController.getSelectedTask().setFinishDate(taskFinishDate);
        }
        mainWindow.refreshTableModel();
        stage.close();
    }

    public void taskWindowCancelButtonHandle(ActionEvent actionEvent) {
        stage.close();
    }
}
