package ru.khasang.cachoeira.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.*;
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
    private static final File DEFAULT_PATH = new File(System.getProperty("user.home") + "/Documents/Cachoeira");

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

    private StringProperty absolutePath = new SimpleStringProperty(this, "absolutePath", DEFAULT_PATH.getAbsolutePath());

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

        nameField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            // Фильтруем все кроме букв и цифр
            if (event.getCharacter().matches("^[^A-Za-z0-9\\s]+$")) {
                event.consume();
            }
        });
        nameField.setText(UIControl.bundle.getString("new_project")); //дефолтовое название проекта
        projectPathField.textProperty().bind(Bindings.concat(absolutePath).concat(File.separator).concat(nameField.textProperty()).concat(".cach"));

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
        directoryChooser.setInitialDirectory(DEFAULT_PATH);
        File file = directoryChooser.showDialog(this.getStage());
        if (file != null) {
            absolutePath.setValue(file.getAbsolutePath());
        }
    }

    @FXML
    private void newProjectCreateButtonHandle(ActionEvent actionEvent) {
        LOGGER.debug("Нажата кнопка \"Создать\".");
        File file = new File(projectPathField.getText());
        if (!file.exists()) {
            createProjectAndLaunchMainWindow(file);
        } else {
            ButtonType yesButton = new ButtonType(UIControl.bundle.getString("yes"));
            ButtonType noButton = new ButtonType(UIControl.bundle.getString("no"), ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Заменить его?", yesButton, noButton);
            alert.setTitle("Cachoeira");
            alert.setHeaderText("Проект с таким именем уже существует!");
            alert.showAndWait()
                    .filter(response -> response == yesButton)
                    .ifPresent(response -> createProjectAndLaunchMainWindow(file));
        }
    }

    @FXML
    private void newProjectCancelButtonHandle(ActionEvent actionEvent) {
        LOGGER.debug("Нажата кнопка \"Отмена\".");
        stage.close();
    }

    private void createProjectAndLaunchMainWindow(File file) {
        // Создаем проект
        uiControl.getController().handleAddProject(nameField.getText(), startDatePicker.getValue(), finishDatePicker.getValue(), descriptionArea.getText());
        // Создаем файл
        DataStoreInterface storeInterface = new DBSchemeManager(uiControl);
        storeInterface.createProjectFile(file.getPath(), uiControl.getController().getProject());
        // Очищаем файл проекта на тот случай, если файл перезаписывается
        storeInterface.eraseAllTables(file);
        storeInterface.saveProjectToFile(uiControl.getFile(), uiControl.getController().getProject());
        // Закрываем это окошко
        stage.close();
        if (uiControl.getStartWindow().getStage().isShowing()) {
            uiControl.getStartWindow().getStage().close(); //закрываем стартовое окно
        }
        uiControl.launchMainWindow(); //запускаем главное окно
    }
}
