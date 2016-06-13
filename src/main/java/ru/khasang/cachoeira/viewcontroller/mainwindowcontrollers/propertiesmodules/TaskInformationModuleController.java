package ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.propertiesmodules;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import ru.khasang.cachoeira.commands.task.*;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.properties.modules.IModule;
import ru.khasang.cachoeira.view.mainwindow.properties.modules.TaskInformation;

import java.time.LocalDate;

public class TaskInformationModuleController implements ModuleController {
    private final TaskInformation module;
    private final MainWindowController controller;

    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<ITask> taskChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener nameFieldInvalidationListener;
    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener costFieldInvalidationListener;
    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener descriptionTextAreaInvalidationListener;

    public TaskInformationModuleController(IModule module, MainWindowController controller) {
        this.module = (TaskInformation) module;
        this.controller = controller;
    }

    @Override
    public void initModule() {
        module.createPane();
        // set disable if selected task is null
        module.disableProperty().bind(controller.selectedTaskProperty().isNull());
        // init listeners
        taskChangeListener = this::selectedTaskObserver;
        nameFieldInvalidationListener = this::nameFieldUnfocused;
        costFieldInvalidationListener = this::costFieldUnfocused;
        descriptionTextAreaInvalidationListener = this::descriptionTextAreaUnfocused;
        // set listener on selected task
        controller.selectedTaskProperty().addListener(new WeakChangeListener<>(taskChangeListener));
        // set handlers on fields events
        module.getNameField().setOnKeyPressed(this::nameFieldHandler);
        module.getDonePercentSlider().setOnMouseReleased(this::donePercentSliderHandler);
        module.getCostField().setOnKeyPressed(this::costFieldHandler);
        module.getDescriptionTextArea().setOnKeyPressed(this::descriptionTextAreaHandler);
        // set listeners to discard changes when field is unfocused
        module.getNameField().focusedProperty().addListener(
                new WeakInvalidationListener(nameFieldInvalidationListener));
        module.getCostField().focusedProperty().addListener(
                new WeakInvalidationListener(costFieldInvalidationListener));
        module.getDescriptionTextArea().focusedProperty().addListener(
                new WeakInvalidationListener(descriptionTextAreaInvalidationListener));
        // set day cells disabled outer valid range
        module.getStartDatePicker().setDayCellFactory(this::makeStartDatePickerCellsDisabled);
        module.getFinishDatePicker().setDayCellFactory(this::makeFinishDatePickerCellsDisabled);
        // set date enabled only by mouse
        module.getStartDatePicker().setEditable(false);
        module.getFinishDatePicker().setEditable(false);
    }

    private void nameFieldHandler(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!module.getNameField().getText().trim().isEmpty()) {
                controller.getCommandExecutor().execute(new RenameTaskCommand(
                        controller.getSelectedTask(),
                        module.getNameField().getText()));
                module.getNameField().getParent().requestFocus();
            }
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            module.getNameField().setText(controller.getSelectedTask().getName());
            module.getNameField().getParent().requestFocus();
        }
    }

    private void donePercentSliderHandler(MouseEvent event) {
        controller.getCommandExecutor().execute(new SetTaskDonePercentCommand(
                controller.getSelectedTask(),
                (int) module.getDonePercentSlider().getValue()
        ));
    }

    private void costFieldHandler(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            controller.getCommandExecutor().execute(new SetTaskCostCommand(
                    controller.getSelectedTask(),
                    Double.parseDouble(module.getCostField().getText())));
            module.getDescriptionTextArea().getParent().requestFocus();
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            module.getCostField().setText(String.valueOf(controller.getSelectedTask().getCost()));
            module.getCostField().getParent().requestFocus();
        }
    }

    private void descriptionTextAreaHandler(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            controller.getCommandExecutor().execute(new SetTaskDescriptionCommand(
                    controller.getSelectedTask(),
                    module.getDescriptionTextArea().getText()));
            module.getDescriptionTextArea().getParent().requestFocus();
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            module.getDescriptionTextArea().setText(controller.getSelectedTask().getDescription());
            module.getDescriptionTextArea().getParent().requestFocus();
        }
    }

    private void selectedTaskObserver(ObservableValue<? extends ITask> observableValue,
                                      ITask oldTask,
                                      ITask newTask) {
        if (newTask != null) {
            module.getNameField().setText(newTask.getName());
            module.getStartDatePicker().setValue(newTask.getStartDate());
            module.getFinishDatePicker().setValue(newTask.getFinishDate());
            module.getDonePercentSlider().setValue(newTask.getDonePercent());
            module.getCostField().setText(String.valueOf(newTask.getCost()));
            module.getDescriptionTextArea().setText(newTask.getDescription());
        }
    }

    private void nameFieldUnfocused(Observable observable) {
        if (module.getNameField().isFocused()) {
            module.getNameField().setText(controller.getSelectedTask().getName());
        }
    }

    private void costFieldUnfocused(Observable observable) {
        if (module.getCostField().isFocused()) {
            module.getCostField().setText(String.valueOf(controller.getSelectedTask().getCost()));
        }
    }

    private void descriptionTextAreaUnfocused(Observable observable) {
        if (module.getDescriptionTextArea().isFocused()) {
            module.getDescriptionTextArea().setText(controller.getSelectedTask().getDescription());
        }
    }

    private DateCell makeStartDatePickerCellsDisabled(DatePicker datePicker) {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate startDate, boolean empty) {
                super.updateItem(startDate, empty);
                if (startDate.isBefore(controller.getProject().getStartDate())) {
                    setDisable(true);
                }
                if (startDate.isEqual(controller.getProject().getFinishDate())
                        || startDate.isAfter(controller.getProject().getFinishDate())) {
                    setDisable(true);
                }
                setOnMousePressed(event -> startDateHandler(startDate));
            }
        };
    }

    private void startDateHandler(LocalDate startDate) {
        controller.getCommandExecutor().execute(new SetTaskStartDateCommand(
                controller.getSelectedTask(),
                startDate
        ));
    }

    private DateCell makeFinishDatePickerCellsDisabled(DatePicker datePicker) {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate finishDate, boolean empty) {
                super.updateItem(finishDate, empty);
                if (finishDate.isBefore(module.getStartDatePicker().getValue().plusDays(1))) {
                    setDisable(true);
                }
                if (finishDate.isEqual(controller.getProject().getFinishDate().plusDays(1))
                        || finishDate.isAfter(controller.getProject().getFinishDate().plusDays(1))) {
                    setDisable(true);
                }
                setOnMouseReleased(event -> finishDateHandler(finishDate));
            }
        };
    }

    private void finishDateHandler(LocalDate finishDate) {
        controller.getCommandExecutor().execute(new SetTaskFinishDateCommand(
                controller.getSelectedTask(),
                finishDate
        ));
    }
}
