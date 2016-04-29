package ru.khasang.cachoeira.view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.khasang.cachoeira.vcontroller.StartWindowController;

import java.io.File;

public class StartWindowView {
    private final static double WINDOW_HEIGHT = 417;
    private final static double WINDOW_WIDTH = 812;
    private final static double TABLE_WIDTH = 341;

    private final StartWindowController controller;

    private Stage stage;
    private TableView<File> recentProjectsTable;
    private TableColumn<File, String> recentProjectsPathColumn;
    private MaterialButton createProjectButton;
    private MaterialButton openProjectButton;

    public StartWindowView(StartWindowController controller) {
        this.controller = controller;
    }

    public void createView() {
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(getClass().getResource("/css/startwindow.css").toExternalForm());
        borderPane.setLeft(createTable());
        borderPane.setCenter(createCentralBox());
        stage = new Stage();
        stage.setHeight(WINDOW_HEIGHT);
        stage.setWidth(WINDOW_WIDTH);
        stage.setScene(new Scene(borderPane));
        stage.setResizable(false);
        stage.setTitle("Cachoeira");
        stage.show();
    }

    private Node createTable() {
        recentProjectsTable = new TableView<>();
        recentProjectsTable.setPrefWidth(TABLE_WIDTH);
        recentProjectsPathColumn = new TableColumn<>("Recent Projects");
        recentProjectsTable.getColumns().add(recentProjectsPathColumn);
        recentProjectsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return recentProjectsTable;
    }

    private Node createCentralBox() {
        VBox centralVerticalPane = new VBox(createImageBox(), createButtonsBox());
        VBox.setVgrow(centralVerticalPane, Priority.ALWAYS);
        return centralVerticalPane;
    }

    private Node createImageBox() {
        ImageView imageView = new ImageView(getClass().getResource("/img/cachoeira.png").toExternalForm());
        imageView.setEffect(new DropShadow(BlurType.TWO_PASS_BOX, Color.rgb(0, 0, 0, 0.8), 4, 0, 0, 2));
        VBox imageVBox = new VBox(imageView);
        VBox.setVgrow(imageVBox, Priority.ALWAYS);
        imageVBox.setAlignment(Pos.CENTER);
        return imageVBox;
    }

    private Node createButtonsBox() {
        createProjectButton = new MaterialButton("CREATE");
        openProjectButton = new MaterialButton("OPEN");
        VBox buttonsVBox = new VBox(20, createProjectButton, openProjectButton);
        VBox.setVgrow(buttonsVBox, Priority.ALWAYS);
        buttonsVBox.setAlignment(Pos.CENTER);
        return buttonsVBox;
    }

    public Stage getStage() {
        return stage;
    }

    public TableView<File> getRecentProjectsTable() {
        return recentProjectsTable;
    }

    public TableColumn<File, String> getRecentProjectsPathColumn() {
        return recentProjectsPathColumn;
    }

    public Button getCreateProjectButton() {
        return createProjectButton;
    }

    public Button getOpenProjectButton() {
        return openProjectButton;
    }
}
