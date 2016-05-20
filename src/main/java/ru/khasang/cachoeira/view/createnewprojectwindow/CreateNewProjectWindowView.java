package ru.khasang.cachoeira.view.createnewprojectwindow;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.khasang.cachoeira.viewcontroller.CreateNewProjectWindowController;
import ru.khasang.cachoeira.view.IView;
import ru.khasang.cachoeira.view.createnewprojectwindow.panes.IButtonsBox;
import ru.khasang.cachoeira.view.createnewprojectwindow.panes.IFieldsPane;

public class CreateNewProjectWindowView implements IView {
    private static final double HEIGHT_WINDOW = 424;
    private static final double WIDTH_WINDOW = 487;

    private final CreateNewProjectWindowController controller;
    private final IFieldsPane fieldsPane;
    private final IButtonsBox buttonsBox;

    private Stage stage;

    public CreateNewProjectWindowView(CreateNewProjectWindowController controller,
                                      IFieldsPane fieldsPane,
                                      IButtonsBox buttonsBox) {
        this.controller = controller;
        this.fieldsPane = fieldsPane;
        this.buttonsBox = buttonsBox;
    }

    @Override
    public void createView() {
        VBox window = new VBox(createFieldsGrid(), createButtonsBox());
        window.setPadding(new Insets(10));
        window.getStylesheets().add(getClass().getResource("/css/startwindow.css").toExternalForm());

        stage = new Stage(StageStyle.UTILITY);
        stage.setHeight(HEIGHT_WINDOW);
        stage.setWidth(WIDTH_WINDOW);
        stage.setScene(new Scene(window));
        stage.setResizable(false);
        stage.setTitle("Creating new project...");
        stage.initOwner(controller.getParentView().getStage());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    private Node createFieldsGrid() {
        fieldsPane.createPane();
        return (Node) fieldsPane;
    }

    private Node createButtonsBox() {
        buttonsBox.createButtonsBox();
        return (Node) buttonsBox;
    }
}
