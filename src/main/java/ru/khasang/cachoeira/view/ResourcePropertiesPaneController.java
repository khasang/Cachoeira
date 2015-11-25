package ru.khasang.cachoeira.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ResourceType;

/**
 * Created by truesik on 24.11.2015.
 */
public class ResourcePropertiesPaneController {
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
    private MainWindow mainWindow;

    public ResourcePropertiesPaneController() {

    }

    @FXML
    private void initialize() {
    }

    public IResource getResource() {
        return resource;
    }

    public void setResource(IResource resource) {
        this.resource = resource;
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void initFields() {
        mainWindow.getResourceTableView().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<IResource>() {
            @Override
            public void changed(ObservableValue<? extends IResource> observable, IResource oldValue, IResource newValue) {
                resource = newValue;
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
}
