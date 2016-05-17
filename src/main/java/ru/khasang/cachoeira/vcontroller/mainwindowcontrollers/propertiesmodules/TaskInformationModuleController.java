package ru.khasang.cachoeira.vcontroller.mainwindowcontrollers.propertiesmodules;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import ru.khasang.cachoeira.commands.CommandControl;
import ru.khasang.cachoeira.commands.task.*;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.properties.modules.IModule;
import ru.khasang.cachoeira.view.mainwindow.properties.modules.TaskInformation;

import java.time.LocalDate;

public class TaskInformationModuleController implements ModuleController {
    private final TaskInformation module;
    private final MainWindowController controller;

    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<TreeItem<ITask>> taskChangeListener;
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
        // init listeners
        taskChangeListener = this::selectedTaskObserver;
        nameFieldInvalidationListener = this::nameFieldUnfocused;
        costFieldInvalidationListener = this::costFieldUnfocused;
        descriptionTextAreaInvalidationListener = this::descriptionTextAreaUnfocused;
        // set listener on selected task
        controller.getTaskTableView().getSelectionModel().selectedItemProperty().addListener(new WeakChangeListener<>(taskChangeListener));
        // set handlers on fields events
        module.getNameField().setOnKeyPressed(this::nameFieldHandler);
        module.getStartDatePicker().setOnAction(this::startDatePickerHandler);
        module.getFinishDatePicker().setOnAction(this::finishDatePickerHandler);
        module.getDonePercentSlider().setOnMouseReleased(this::donePercentSliderHandler);
        module.getCostField().setOnKeyPressed(this::costFieldHandler);
        module.getDescriptionTextArea().setOnKeyPressed(this::descriptionTextAreaHandler);
        // set listeners to discard changes when field is unfocused
        module.getNameField().focusedProperty().addListener(new WeakInvalidationListener(nameFieldInvalidationListener));
        module.getCostField().focusedProperty().addListener(new WeakInvalidationListener(costFieldInvalidationListener));
        module.getDescriptionTextArea().focusedProperty().addListener(new WeakInvalidationListener(descriptionTextAreaInvalidationListener));
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
                CommandControl.getInstance().execute(new RenameTaskCommand(
                        controller.getTaskTableView().getSelectionModel().getSelectedItem().getValue(),
                        module.getNameField().getText()));
                module.getNameField().getParent().requestFocus();
            }
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            module.getNameField().setText(controller.getTaskTableView().getSelectionModel().getSelectedItem().getValue().getName());
            module.getNameField().getParent().requestFocus();
        }
    }

    private void startDatePickerHandler(ActionEvent event) {
        CommandControl.getInstance().execute(new SetTaskStartDateCommand(
                controller.getTaskTableView().getSelectionModel().getSelectedItem().getValue(),
                module.getStartDatePicker().getValue()
        ));
    }

    private void finishDatePickerHandler(ActionEvent event) {
        CommandControl.getInstance().execute(new SetTaskFinishDateCommand(
                controller.getTaskTableView().getSelectionModel().getSelectedItem().getValue(),
                module.getFinishDatePicker().getValue()
        ));
    }

    private void donePercentSliderHandler(MouseEvent event) {
        CommandControl.getInstance().execute(new SetTaskDonePercentCommand(
                controller.getTaskTableView().getSelectionModel().getSelectedItem().getValue(),
                (int) module.getDonePercentSlider().getValue()
        ));
    }

    private void costFieldHandler(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            CommandControl.getInstance().execute(new SetTaskCostCommand(
                    controller.getTaskTableView().getSelectionModel().getSelectedItem().getValue(),
                    Double.parseDouble(module.getCostField().getText())));
            module.getDescriptionTextArea().getParent().requestFocus();
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            module.getCostField().setText(String.valueOf(controller.getTaskTableView().getSelectionModel().getSelectedItem().getValue().getCost()));
            module.getCostField().getParent().requestFocus();
        }
    }

    private void descriptionTextAreaHandler(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            CommandControl.getInstance().execute(new SetTaskDescriptionCommand(
                    controller.getTaskTableView().getSelectionModel().getSelectedItem().getValue(),
                    module.getDescriptionTextArea().getText()));
            module.getDescriptionTextArea().getParent().requestFocus();
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            module.getDescriptionTextArea().setText(controller.getTaskTableView().getSelectionModel().getSelectedItem().getValue().getDescription());
            module.getDescriptionTextArea().getParent().requestFocus();
        }
    }

    private void selectedTaskObserver(ObservableValue<? extends TreeItem<ITask>> observableValue, TreeItem<ITask> oldTaskItem, TreeItem<ITask> newTaskItem) {
        module.getNameField().setText(newTaskItem.getValue().getName());
        module.getStartDatePicker().setValue(newTaskItem.getValue().getStartDate());
        module.getFinishDatePicker().setValue(newTaskItem.getValue().getFinishDate());
        module.getDonePercentSlider().setValue(newTaskItem.getValue().getDonePercent());
        module.getCostField().setText(String.valueOf(newTaskItem.getValue().getCost()));
        module.getDescriptionTextArea().setText(newTaskItem.getValue().getDescription());
    }

    private void nameFieldUnfocused(Observable observable) {
        if (module.getNameField().isFocused()) {
            module.getNameField().setText(controller.getTaskTableView().getSelectionModel().getSelectedItem().getValue().getName());
        }
    }

    private void costFieldUnfocused(Observable observable) {
        if (module.getCostField().isFocused()) {
            module.getCostField().setText(String.valueOf(controller.getTaskTableView().getSelectionModel().getSelectedItem().getValue().getCost()));
        }
    }

    private void descriptionTextAreaUnfocused(Observable observable) {
        if (module.getDescriptionTextArea().isFocused()) {
            module.getDescriptionTextArea().setText(controller.getTaskTableView().getSelectionModel().getSelectedItem().getValue().getDescription());
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
                if (startDate.isEqual(controller.getProject().getFinishDate()) || startDate.isAfter(controller.getProject().getFinishDate())) {
                    setDisable(true);
                }
            }
        };
    }

    private DateCell makeFinishDatePickerCellsDisabled(DatePicker datePicker) {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate finishDate, boolean empty) {
                super.updateItem(finishDate, empty);
                if (finishDate.isBefore(module.getStartDatePicker().getValue().plusDays(1))) {
                    setDisable(true);
                }
                if (finishDate.isEqual(controller.getProject().getFinishDate().plusDays(1)) || finishDate.isAfter(controller.getProject().getFinishDate().plusDays(1))) {
                    setDisable(true);
                }
            }
        };
    }
}
