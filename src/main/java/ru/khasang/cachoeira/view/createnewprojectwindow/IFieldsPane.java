package ru.khasang.cachoeira.view.createnewprojectwindow;

import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public interface IFieldsPane {
    void createPane();

    TextField getProjectNameField();

    TextField getProjectPathField();

    DatePicker getStartDatePicker();

    DatePicker getFinishDatePicker();

    TextArea getDescriptionTextArea();

    Button getPathChooserButton();
}
