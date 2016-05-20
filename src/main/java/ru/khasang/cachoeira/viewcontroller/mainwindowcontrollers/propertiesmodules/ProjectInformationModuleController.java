package ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.propertiesmodules;

import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.khasang.cachoeira.commands.project.RenameProjectCommand;
import ru.khasang.cachoeira.commands.project.SetProjectDescriptionCommand;
import ru.khasang.cachoeira.commands.project.SetProjectFinishDateCommand;
import ru.khasang.cachoeira.commands.project.SetProjectStartDateCommand;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.properties.modules.IModule;
import ru.khasang.cachoeira.view.mainwindow.properties.modules.ProjectInformation;

import java.time.LocalDate;

public class ProjectInformationModuleController implements ModuleController {
    private final ProjectInformation module;
    private final MainWindowController controller;

    public ProjectInformationModuleController(IModule module, MainWindowController controller) {
        this.module = (ProjectInformation) module;
        this.controller = controller;
    }

    @Override
    public void initModule() {
        module.getNameField().setText(controller.getProject().getName());
        module.getStartDatePicker().setValue(controller.getProject().getStartDate());
        module.getFinishDatePicker().setValue(controller.getProject().getFinishDate());
        module.getDescriptionTextArea().setText(controller.getProject().getDescription());

        controller.getProject().nameProperty().addListener(this::projectNameObserver);
        controller.getProject().startDateProperty().addListener(this::projectStartDateObserver);
        controller.getProject().finishDateProperty().addListener(this::projectFinishDateObserver);
        controller.getProject().descriptionProperty().addListener(this::projectDescriptionObserver);
        // set handlers on fields events
        module.getNameField().setOnKeyPressed(this::projectNameFieldHandler);
        module.getStartDatePicker().setOnAction(this::projectStartDatePickerHandler);
        module.getFinishDatePicker().setOnAction(this::projectFinishDatePickerHandler);
        module.getDescriptionTextArea().setOnKeyPressed(this::projectDescriptionTextAreaHandler);
        // set listeners to discard changes when field is unfocused
        module.getNameField().focusedProperty().addListener(this::projectNameFieldUnfocused);
        module.getDescriptionTextArea().focusedProperty().addListener(this::projectDescriptionTextAreaUnfocused);
        // set date cells disabled outer valid range
        module.getFinishDatePicker().setDayCellFactory(this::makeFinishDatePickerCellsDisabledBeforeStartDate);
        // set date enabled only by mouse
        module.getStartDatePicker().setEditable(false);
        module.getFinishDatePicker().setEditable(false);
    }

    private void projectNameObserver(Observable observable) {
        module.getNameField().setText(controller.getProject().getName());
    }

    private void projectStartDateObserver(Observable observable) {
        module.getStartDatePicker().setValue(controller.getProject().getStartDate());
    }

    private void projectFinishDateObserver(Observable observable) {
        module.getFinishDatePicker().setValue(controller.getProject().getFinishDate());
    }

    private void projectDescriptionObserver(Observable observable) {
        module.getDescriptionTextArea().setText(controller.getProject().getDescription());
    }

    private void projectNameFieldHandler(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!module.getNameField().getText().trim().isEmpty()) {
                controller.getCommandExecutor().execute(new RenameProjectCommand(
                        controller.getProject(),
                        module.getNameField().getText()));
                // Убираем фокусировку с поля наименования задачи
                module.getNameField().getParent().requestFocus();
            }
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            module.getNameField().setText(controller.getProject().getName());
            module.getNameField().getParent().requestFocus();
        }
    }

    private void projectStartDatePickerHandler(ActionEvent event) {
        controller.getCommandExecutor().execute(new SetProjectStartDateCommand(
                controller.getProject(),
                module.getStartDatePicker().getValue()));
    }

    private void projectFinishDatePickerHandler(ActionEvent event) {
        controller.getCommandExecutor().execute(new SetProjectFinishDateCommand(
                controller.getProject(),
                module.getFinishDatePicker().getValue()));
    }

    private void projectDescriptionTextAreaHandler(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            controller.getCommandExecutor().execute(new SetProjectDescriptionCommand(
                    controller.getProject(),
                    module.getDescriptionTextArea().getText()));
            module.getDescriptionTextArea().getParent().requestFocus();
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            module.getDescriptionTextArea().setText(controller.getProject().getDescription());
            module.getDescriptionTextArea().getParent().requestFocus();
        }
    }

    private void projectNameFieldUnfocused(Observable observable) {
        if (!module.getNameField().isFocused()) {
            module.getNameField().setText(controller.getProject().getName());
        }
    }

    private void projectDescriptionTextAreaUnfocused(Observable observable) {
        if (!module.getDescriptionTextArea().isFocused()) {
            module.getDescriptionTextArea().setText(controller.getProject().getDescription());
        }
    }

    private DateCell makeFinishDatePickerCellsDisabledBeforeStartDate(DatePicker datePicker) {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate finishDate, boolean empty) {
                super.updateItem(finishDate, empty);
                if (finishDate.isBefore(module.getStartDatePicker().getValue().plusDays(1))) {
                    setDisable(true);
                }
            }
        };
    }
}
