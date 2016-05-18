package ru.khasang.cachoeira.vcontroller.mainwindowcontrollers.propertiesmodules;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.khasang.cachoeira.commands.CommandControl;
import ru.khasang.cachoeira.commands.resource.RenameResourceCommand;
import ru.khasang.cachoeira.commands.resource.SetResourceDescriptionCommand;
import ru.khasang.cachoeira.commands.resource.SetResourceEmailCommand;
import ru.khasang.cachoeira.commands.resource.SetResourceTypeCommand;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ResourceType;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.properties.modules.IModule;
import ru.khasang.cachoeira.view.mainwindow.properties.modules.ResourceInformation;


public class ResourceInformationModuleController implements ModuleController {
    private final ResourceInformation module;
    private final MainWindowController controller;

    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<TreeItem<IResource>> resourceChangeListener;

    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener nameFieldInvalidationListener;
    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener emailFieldInvalidationListener;
    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener descriptionTextAreaInvalidationListener;

    public ResourceInformationModuleController(IModule module, MainWindowController controller) {
        this.module = (ResourceInformation) module;
        this.controller = controller;
    }

    @Override
    public void initModule() {
        module.getResourceTypeComboBox().setItems(FXCollections.observableArrayList(ResourceType.values()));

        resourceChangeListener = this::selectedResourceObserver;
        nameFieldInvalidationListener = this::nameFieldUnfocused;
        emailFieldInvalidationListener = this::emailFieldUnfocused;
        descriptionTextAreaInvalidationListener = this::descriptionTextAreaUnfocused;

        controller.getResourceTableView().getSelectionModel().selectedItemProperty().addListener(new WeakChangeListener<>(resourceChangeListener));

        module.getNameField().setOnKeyPressed(this::nameFieldObserver);
        module.getResourceTypeComboBox().setOnAction(this::resourceTypeComboBoxObserver);
        module.getEmailField().setOnKeyPressed(this::emailFieldObserver);
        module.getDescriptionTextArea().setOnKeyPressed(this::descriptionTextAreaObserver);

        module.getNameField().focusedProperty().addListener(new WeakInvalidationListener(nameFieldInvalidationListener));
        module.getEmailField().focusedProperty().addListener(new WeakInvalidationListener(emailFieldInvalidationListener));
        module.getDescriptionTextArea().focusedProperty().addListener(new WeakInvalidationListener(descriptionTextAreaInvalidationListener));
    }

    private void selectedResourceObserver(ObservableValue<? extends TreeItem<IResource>> observableValue, TreeItem<IResource> oldResourceItem, TreeItem<IResource> newResourceItem) {
        module.getNameField().setText(newResourceItem.getValue().getName());
        module.getResourceTypeComboBox().setValue(newResourceItem.getValue().getType());
        module.getEmailField().setText(newResourceItem.getValue().getEmail());
        module.getDescriptionTextArea().setText(newResourceItem.getValue().getDescription());
    }

    private void nameFieldObserver(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!module.getNameField().getText().trim().isEmpty()) {
                CommandControl.getInstance().execute(new RenameResourceCommand(
                        controller.getResourceTableView().getSelectionModel().getSelectedItem().getValue(),
                        module.getNameField().getText()));
                module.getNameField().getParent().requestFocus();
            }
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            module.getNameField().setText(controller.getResourceTableView().getSelectionModel().getSelectedItem().getValue().getName());
            module.getNameField().getParent().requestFocus();
        }
    }

    private void resourceTypeComboBoxObserver(ActionEvent event) {
        CommandControl.getInstance().execute(new SetResourceTypeCommand(
                controller.getResourceTableView().getSelectionModel().getSelectedItem().getValue(),
                module.getResourceTypeComboBox().getValue()));
    }

    private void emailFieldObserver(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            CommandControl.getInstance().execute(new SetResourceEmailCommand(
                    controller.getResourceTableView().getSelectionModel().getSelectedItem().getValue(),
                    module.getEmailField().getText()));
            module.getEmailField().getParent().requestFocus();
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            module.getEmailField().setText(controller.getResourceTableView().getSelectionModel().getSelectedItem().getValue().getEmail());
            module.getEmailField().getParent().requestFocus();
        }
    }

    private void descriptionTextAreaObserver(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            CommandControl.getInstance().execute(new SetResourceDescriptionCommand(
                    controller.getResourceTableView().getSelectionModel().getSelectedItem().getValue(),
                    module.getDescriptionTextArea().getText()));
            module.getDescriptionTextArea().getParent().requestFocus();
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            module.getDescriptionTextArea().setText(controller.getResourceTableView().getSelectionModel().getSelectedItem().getValue().getDescription());
            module.getDescriptionTextArea().getParent().requestFocus();
        }
    }

    private void nameFieldUnfocused(Observable observable) {
        if (!module.getNameField().isFocused()) {
            module.getNameField().setText(controller.getResourceTableView().getSelectionModel().getSelectedItem().getValue().getName());
        }
    }

    private void emailFieldUnfocused(Observable observable) {
        if (!module.getEmailField().isFocused()) {
            module.getEmailField().setText(controller.getResourceTableView().getSelectionModel().getSelectedItem().getValue().getEmail());
        }
    }

    private void descriptionTextAreaUnfocused(Observable observable) {
        if (!module.getDescriptionTextArea().isFocused()) {
            module.getDescriptionTextArea().setText(controller.getResourceTableView().getSelectionModel().getSelectedItem().getValue().getDescription());
        }
    }
}