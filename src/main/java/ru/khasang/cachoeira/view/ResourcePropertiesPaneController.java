package ru.khasang.cachoeira.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ResourceType;

/**
 * Created by truesik on 24.11.2015.
 */
public class ResourcePropertiesPaneController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label resourceTypeLabel;

    //Информация
    @FXML
    private VBox propertiesPane;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private ComboBox<ResourceType> resourceTypeComboBox;

    //Привязанные задачи
    @FXML
    private TableView taskTableView;
    @FXML
    private TableColumn taskNameColumn;
    @FXML
    private TableColumn taskCheckboxColumn;

    private IResource resource;
    private IController controller;

    public ResourcePropertiesPaneController() {

    }

    @FXML
    private void initialize() {
        nameLabel.setLabelFor(nameField);
        emailLabel.setLabelFor(emailField);
        resourceTypeLabel.setLabelFor(resourceTypeComboBox);
    }

    public IResource getResource() {
        return resource;
    }

    public void setResource(IResource resource) {
        this.resource = resource;
    }


    public void initFields() {
        controller.selectedResourceProperty().addListener(new ChangeListener<IResource>() {
            @Override
            public void changed(ObservableValue<? extends IResource> observable, IResource oldValue, IResource newValue) {
                propertiesPane.setDisable(false);
                nameField.textProperty().bindBidirectional(newValue.nameProperty());
                emailField.textProperty().bindBidirectional(newValue.emailProperty());
                resourceTypeComboBox.valueProperty().bindBidirectional(newValue.resourceTypeProperty());
            }
        });
        if (resource != null) {
            nameField.textProperty().bindBidirectional(resource.nameProperty());
            emailField.textProperty().bindBidirectional(resource.emailProperty());
            resourceTypeComboBox.valueProperty().bindBidirectional(resource.resourceTypeProperty());
        } else {
            propertiesPane.setDisable(true);
        }
    }

    public void setController(IController controller) {
        this.controller = controller;
    }
}
