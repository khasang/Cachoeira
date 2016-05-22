package ru.khasang.cachoeira.viewcontroller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import ru.khasang.cachoeira.data.DataService;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.Project;
import ru.khasang.cachoeira.view.IView;
import ru.khasang.cachoeira.view.createnewprojectwindow.CreateNewProjectWindowView;
import ru.khasang.cachoeira.view.createnewprojectwindow.panes.ButtonsBox;
import ru.khasang.cachoeira.view.createnewprojectwindow.panes.FieldsPane;
import ru.khasang.cachoeira.view.createnewprojectwindow.panes.IButtonsBox;
import ru.khasang.cachoeira.view.createnewprojectwindow.panes.IFieldsPane;

import java.io.File;
import java.time.LocalDate;

public class CreateNewProjectWindowController {
    private static final File DEFAULT_PATH = new File(System.getProperty("user.home") + "/Documents/Cachoeira");

    private final IView view;
    private final IView parentView;

    private IFieldsPane fieldsPane;
    private IButtonsBox buttonsBox;
    private StringProperty absolutePath = new SimpleStringProperty(this, "absolutePath", DEFAULT_PATH.getAbsolutePath());

    public CreateNewProjectWindowController(IView parentView) {
        this.parentView = parentView;
        fieldsPane = new FieldsPane();
        buttonsBox = new ButtonsBox();
        view = new CreateNewProjectWindowView(this, fieldsPane, buttonsBox);
    }

    public void launch() {
        view.createView();
        this.initFields();
        this.attachButtonsEvents();
    }

    private void initFields() {
        buttonsBox.getCreateNewProjectButton().disableProperty().bind(
                fieldsPane.getProjectNameField().textProperty().isEmpty()
                        .or(fieldsPane.getProjectPathField().textProperty().isEmpty())); //рубим нажимательность кнопки, если поле с именем пустует

        // Фильтруем все кроме букв, цифр и пробела (в том числе и из буфера обмена)
        fieldsPane.getProjectNameField().setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().matches("^[^A-Za-z0-9\\s]+$")) {
                return null;
            }
            return change;
        }));
        fieldsPane.getProjectNameField().setText("New Project"); //дефолтовое название проекта
        fieldsPane.getProjectPathField().textProperty().bind(
                Bindings.concat(absolutePath).concat(File.separator).concat(fieldsPane.getProjectNameField().textProperty()).concat(".cach"));

        // Отрубаем возможность ввода дат с клавиатуры воизбежание пустого поля
        fieldsPane.getStartDatePicker().setEditable(false);
        fieldsPane.getFinishDatePicker().setEditable(false);

        fieldsPane.getStartDatePicker().setValue(LocalDate.now()); //по дефолту сегодняшняя дата
        fieldsPane.getFinishDatePicker().setValue(fieldsPane.getStartDatePicker().getValue().plusDays(28));
        // Конечная дата всегда после начальной
        fieldsPane.getStartDatePicker().valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.isEqual(fieldsPane.getFinishDatePicker().getValue()) || newValue.isAfter(fieldsPane.getFinishDatePicker().getValue())) {
                fieldsPane.getFinishDatePicker().setValue(newValue.plusDays(1));
            }
        }));

        fieldsPane.getFinishDatePicker().setDayCellFactory(this::makeFinishDatePickerCellsDisabledBeforeStartDate);
    }

    private DateCell makeFinishDatePickerCellsDisabledBeforeStartDate(DatePicker datePicker) {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate finishDate, boolean empty) {
                super.updateItem(finishDate, empty);
                if (finishDate.isBefore(fieldsPane.getStartDatePicker().getValue().plusDays(1))) {
                    setDisable(true);
                }
            }
        };
    }

    private void attachButtonsEvents() {
        fieldsPane.getPathChooserButton().setOnAction(this::pathChooserHandler);
        buttonsBox.getCreateNewProjectButton().setOnAction(this::createProjectHandler);
        buttonsBox.getCancelButton().setOnAction(this::cancelHandler);
    }

    private void pathChooserHandler(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(DEFAULT_PATH);
        File file = directoryChooser.showDialog(view.getStage());
        if (file != null) {
            absolutePath.setValue(file.getAbsolutePath());
        }
    }

    private void createProjectHandler(ActionEvent event) {
        File file = new File(fieldsPane.getProjectPathField().getText());
        if (!file.exists()) {
            createProjectAndLaunchMainWindow(file);
        } else {
            ButtonType yesButton = new ButtonType("yes");
            ButtonType noButton = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Заменить его?", yesButton, noButton);
            alert.setTitle("Cachoeira");
            alert.setHeaderText("Проект с таким именем уже существует!");
            alert.showAndWait()
                    .filter(response -> response == yesButton)
                    .ifPresent(response -> createProjectAndLaunchMainWindow(file));
        }
    }

    private void cancelHandler(ActionEvent event) {
        view.getStage().close();
    }

    private void createProjectAndLaunchMainWindow(File file) {
        // Создаем проект
        IProject project = new Project(
                fieldsPane.getProjectNameField().getText(),
                fieldsPane.getStartDatePicker().getValue(),
                fieldsPane.getFinishDatePicker().getValue(),
                fieldsPane.getDescriptionTextArea().getText());
//        // TODO: 07.05.2016 doesn't work
////        if (view.getStage().isShowing()) {
////            view.getStage().close(); //закрываем стартовое окно
////        }
        DataService.getInstance().createProject(project, file);
        // Закрываем это окошко
        view.getStage().close();
    }

    public IView getParentView() {
        return parentView;
    }
}
