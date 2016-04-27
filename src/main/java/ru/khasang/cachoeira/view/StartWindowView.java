package ru.khasang.cachoeira.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class StartWindowView extends Application{
    private final static double WINDOW_HEIGHT = 417;
    private final static double WINDOW_WIDTH = 812;
    private final static double TABLE_WIDTH = 341;

    private Stage stage;
    private TableView<File> recentProjectsTable;
    private TableColumn<File, String> recentProjectsPathColumn;
    private Button createProjectButton;
    private Button openProjectButton;

    public StartWindowView() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StartWindowView startWindowView = new StartWindowView();
        startWindowView.createView();
    }

    public void createView() {
        BorderPane borderPane = new BorderPane();
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
        VBox imageVBox = new VBox(imageView);
        VBox.setVgrow(imageVBox, Priority.ALWAYS);
        imageVBox.setAlignment(Pos.CENTER);
        return imageVBox;
    }

    private Node createButtonsBox() {
        createProjectButton = new Button("Create");
        openProjectButton = new Button("Open");
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

    public static void main(String[] args) {
        Application.launch();

    }
}
