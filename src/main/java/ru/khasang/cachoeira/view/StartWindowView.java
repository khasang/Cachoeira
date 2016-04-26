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

    private Stage stage;
    private BorderPane borderPane;
    private TableView<File> recentProjectsTable;
    private TableColumn<File, String> recentProjectsPathColumn;
    private VBox centralVerticalPane;
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
        borderPane = new BorderPane();
        recentProjectsTable = new TableView<>();
        recentProjectsPathColumn = new TableColumn<>("Recent Projects");

        recentProjectsTable.getColumns().add(recentProjectsPathColumn);
        recentProjectsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        borderPane.setLeft(recentProjectsTable);
        centralVerticalPane = new VBox(createImageBox(), createButtonsBox());
        VBox.setVgrow(centralVerticalPane, Priority.ALWAYS);
        borderPane.setCenter(centralVerticalPane);

        stage = new Stage();
        stage.setScene(new Scene(borderPane, WINDOW_WIDTH, WINDOW_HEIGHT));
        stage.setResizable(false);
        stage.setTitle("Cachoeira");
        stage.show();
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

    public static void main(String[] args) {
        Application.launch();

    }
}
