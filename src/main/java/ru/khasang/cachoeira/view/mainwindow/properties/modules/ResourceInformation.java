package ru.khasang.cachoeira.view.mainwindow.properties.modules;

import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import ru.khasang.cachoeira.model.ResourceType;

public class ResourceInformation extends GridPane implements IModule {
    private static final double FIRST_COLUMN_PERCENT_WIDTH = 30;
    private static final double SECOND_COLUMN_PERCENT_WIDTH = 70;

    private Label nameLabel;
    private Label emailLabel;
    private Label resourceTypeLabel;
    private Label descriptionLabel;

    private TextField nameField;
    private TextField emailField;
    private ComboBox<ResourceType> resourceTypeComboBox;
    private TextArea descriptionTextArea;

    public ResourceInformation() {
        nameLabel = new Label("Name");
        emailLabel = new Label("E-Mail");
        resourceTypeLabel = new Label("Resource Type");
        descriptionLabel = new Label("Description");

        nameField = new TextField();
        emailField = new TextField();
        resourceTypeComboBox = new ComboBox<>();
        descriptionTextArea = new TextArea();
    }

    @Override
    public void createPane() {
        ColumnConstraints firstColumnConstrains = new ColumnConstraints();
        ColumnConstraints secondColumnConstrains = new ColumnConstraints();
        firstColumnConstrains.setPercentWidth(FIRST_COLUMN_PERCENT_WIDTH);
        secondColumnConstrains.setPercentWidth(SECOND_COLUMN_PERCENT_WIDTH);
        secondColumnConstrains.setHgrow(Priority.ALWAYS);

        this.getColumnConstraints().addAll(firstColumnConstrains, secondColumnConstrains);
        this.addRow(0, nameLabel, nameField);
        this.addRow(1, emailLabel, emailField);
        this.addRow(2, resourceTypeLabel, resourceTypeComboBox);
        this.addRow(3, descriptionLabel, descriptionTextArea);

        this.setVgap(10);
        this.setHgap(10);
        this.setPadding(new Insets(10));

        emailField.setPrefWidth(350);
        resourceTypeComboBox.setPrefWidth(350);
        GridPane.setValignment(descriptionLabel, VPos.TOP);
    }

    public TextField getNameField() {
        return nameField;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public ComboBox<ResourceType> getResourceTypeComboBox() {
        return resourceTypeComboBox;
    }

    public TextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }
}
