package ru.khasang.cachoeira.view;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Этот класс является контроллером для вью {@link fxml/NewProjectWindow.fxml}.
 * С помощью этого класса задаются параметры и создается новый проект.
 */
public class NewProjectWindow implements IWindow {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewProjectWindow.class.getName());

    private UIControl UIControl;
    @FXML
    private TextField nameField;
    @FXML
    private TextField projectPathField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker finishDatePicker;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Button createNewProjectButton;

    private Parent root = null;
    private Stage stage;

    public NewProjectWindow(UIControl UIControl) {
        this.UIControl = UIControl;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NewProjectWindow.fxml"));    //грузим макет окна
        fxmlLoader.setController(this);                                                             //говорим макету, что этот класс является его контроллером
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void launch() {
        stage = new Stage(StageStyle.UTILITY);
        if (root != null) {
            stage.setScene(new Scene(root));
        }
        stage.setResizable(false);
        stage.initOwner(UIControl.getStartWindow().getStage());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
        stage.setTitle("Новый проект");

        LOGGER.debug("Открыто окно создания нового проекта.");

        createNewProjectButton.disableProperty().bind(nameField.textProperty().isEmpty()); //рубим нажимательность кнопки, если поле с именем пустует

        nameField.setText("Новый проект"); //дефолтовое название проекта

        /** Отрубаем возможность ввода дат с клавиатуры воизбежание пустого поля */
        startDatePicker.setEditable(false);
        finishDatePicker.setEditable(false);

        startDatePicker.setValue(LocalDate.now()); //по дефолту сегодняшняя дата
        finishDatePicker.setValue(startDatePicker.getValue().plusDays(28));
        /** Конечная дата всегда после начальной */
        startDatePicker.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.isEqual(finishDatePicker.getValue()) || newValue.isAfter(finishDatePicker.getValue())) {
                finishDatePicker.setValue(newValue.plusDays(1));
            }
        }));

        finishDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(startDatePicker.getValue().plusDays(1))) {
                            setDisable(true);
                        }
                    }
                };
            }
        });
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @FXML
    private void newProjectPathChooserButtonHandle(ActionEvent actionEvent) {

    }

    @FXML
    private void newProjectCreateButtonHandle(ActionEvent actionEvent) {
        LOGGER.debug("Нажата кнопка \"Создать\".");
        UIControl.getController().notifyAddProject(nameField.getText(), startDatePicker.getValue(), finishDatePicker.getValue(), descriptionArea.getText()); //создаем проект
        stage.close(); // закрываем это окошко
        if (UIControl.getStartWindow().getStage().isShowing()) {
            UIControl.getStartWindow().getStage().close(); //закрываем стартовое окно
        }
        UIControl.launchMainWindow(); //запускаем главное окно
    }

    @FXML
    private void newProjectCancelButtonHandle(ActionEvent actionEvent) {
        LOGGER.debug("Нажата кнопка \"Отмена\".");
        stage.close();
    }
}
