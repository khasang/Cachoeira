package ru.khasang.cachoeira.view.startwindow;

import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.view.MaterialButton;

public class ButtonsBox extends VBox implements IButtonsBox {
    private MaterialButton createProjectButton;
    private MaterialButton openProjectButton;

    public ButtonsBox() {
        createProjectButton = new MaterialButton("CREATE");
        openProjectButton = new MaterialButton("OPEN");
    }

    @Override
    public void createButtonsBox() {
        this.getChildren().addAll(createProjectButton, openProjectButton);
        this.setSpacing(20);
        VBox.setVgrow(this, Priority.ALWAYS);
        this.setAlignment(Pos.CENTER);
    }

    @Override
    public MaterialButton getCreateProjectButton() {
        return createProjectButton;
    }

    @Override
    public MaterialButton getOpenProjectButton() {
        return openProjectButton;
    }
}
