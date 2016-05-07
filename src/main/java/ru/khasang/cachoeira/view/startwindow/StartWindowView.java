package ru.khasang.cachoeira.view.startwindow;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
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
import ru.khasang.cachoeira.view.IView;

import java.io.File;

public class StartWindowView implements IView{
    private final static double WINDOW_HEIGHT = 417;
    private final static double WINDOW_WIDTH = 812;
    private final static double TABLE_WIDTH = 341;

    private final StartWindowController controller;
    private final TableView<File> recentProjectsTable;
    private final IButtonsBox buttonsBox;

    private Stage stage;

    public StartWindowView(StartWindowController controller,
                           TableView<File> recentProjectsTable,
                           IButtonsBox buttonsBox) {
        this.controller = controller;
        this.recentProjectsTable = recentProjectsTable;
        this.buttonsBox = buttonsBox;
    }

    @Override
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

    @Override
    public Stage getStage() {
        return stage;
    }

    private Node createTable() {
        recentProjectsTable.setPrefWidth(TABLE_WIDTH);
        TableColumn<File, String> recentProjectsPathColumn = new TableColumn<>("Recent Projects");
        recentProjectsPathColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPath()));
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
        buttonsBox.createButtonsBox();
        return (Node) buttonsBox;
    }
}
