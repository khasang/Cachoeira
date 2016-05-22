package ru.khasang.cachoeira.view.createnewprojectwindow.panes;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import ru.khasang.cachoeira.view.MaterialButton;

public class ButtonsBox extends HBox implements IButtonsBox {
    private MaterialButton createNewProjectButton;
    private MaterialButton cancelButton;

    public ButtonsBox() {
        createNewProjectButton = new MaterialButton("Create");
        cancelButton = new MaterialButton("Cancel");
    }

    @Override
    public void createButtonsBox() {
        createNewProjectButton.setDefaultButton(true);
        cancelButton.setCancelButton(true);
        this.getChildren().addAll(createNewProjectButton, cancelButton);
        this.setSpacing(20);
        HBox.setHgrow(this, Priority.NEVER);
        this.setAlignment(Pos.TOP_RIGHT);
    }

    @Override
    public MaterialButton getCreateNewProjectButton() {
        return createNewProjectButton;
    }

    @Override
    public MaterialButton getCancelButton() {
        return cancelButton;
    }
}
