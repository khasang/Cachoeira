package ru.khasang.cachoeira.view;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.data.DBSchemeManager;
import ru.khasang.cachoeira.data.DataStoreInterface;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Этот класс является контроллером для вью {@link fxml/NewProjectWindow.fxml}.
 * С помощью этого класса задаются параметры и создается новый проект.
 */
public class NewProjectWindow implements IWindow {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewProjectWindow.class.getName());

    private UIControl uiControl;
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

    private File pathToFile = new File(System.getProperty("user.home") + "/Documents/Cachoeira");

    public NewProjectWindow(UIControl uiControl) {
        this.uiControl = uiControl;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NewProjectWindow.fxml"));    //грузим макет окна
        fxmlLoader.setResources(UIControl.bundle);
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
        stage.initOwner(uiControl.getStartWindow().getStage());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
        stage.setTitle(UIControl.bundle.getString("new_project"));

        LOGGER.debug("Открыто окно создания нового проекта.");

        createNewProjectButton.disableProperty().bind(nameField.textProperty().isEmpty().or(projectPathField.textProperty().isEmpty())); //рубим нажимательность кнопки, если поле с именем пустует

        nameField.setText(UIControl.bundle.getString("new_project")); //дефолтовое название проекта
        projectPathField.textProperty().bind(Bindings.concat(pathToFile).concat(File.separator).concat(nameField.textProperty()).concat(".cach"));

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
        DirectoryChooser directoryChooser = new DirectoryChooser();
        pathToFile = directoryChooser.showDialog(this.getStage());
    }

    @FXML
    private void newProjectCreateButtonHandle(ActionEvent actionEvent) {
        LOGGER.debug("Нажата кнопка \"Создать\".");
        // Создаем проект
        uiControl.getController().handleAddProject(nameField.getText(), startDatePicker.getValue(), finishDatePicker.getValue(), descriptionArea.getText());
        // Создаем файл
        DataStoreInterface storeInterface = new DBSchemeManager(uiControl);
        storeInterface.createProjectFile(projectPathField.getText(), uiControl.getController().getProject());
        storeInterface.saveProjectToFile(uiControl.getFile(), uiControl.getController().getProject());
        // Закрываем это окошко
        stage.close();
        if (uiControl.getStartWindow().getStage().isShowing()) {
            uiControl.getStartWindow().getStage().close(); //закрываем стартовое окно
        }
        uiControl.launchMainWindow(); //запускаем главное окно
    }

    @FXML
    private void newProjectCancelButtonHandle(ActionEvent actionEvent) {
        LOGGER.debug("Нажата кнопка \"Отмена\".");
        stage.close();
    }
}
