package ru.khasang.cachoeira.view.mainwindow.diagram;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.diagram.buttonbox.AbstractButtonsBox;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.AbstractGanttPlan;
import ru.khasang.cachoeira.view.mainwindow.diagram.tables.AbstractTableView;

public abstract class TableAndGanttPane extends VBox {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableAndGanttPane.class.getName());

    private static final double ROW_HEIGHT = 31;

    protected MainWindowController controller;
    protected AbstractTableView tableView;
    protected AbstractGanttPlan ganttPlan;
    protected AbstractButtonsBox buttonBox;
    protected SplitPane splitPane;

    public void createPane() {
        splitPane.getItems().addAll(createTableView(), createGanttPLan());
        VBox.setVgrow(splitPane, Priority.ALWAYS);
        HBox hBox = createButtonsBox();
        VBox.setVgrow(hBox, Priority.NEVER);
        this.getChildren().addAll(splitPane, hBox);
        LOGGER.debug("Created.");
    }

    private Node createGanttPLan() {
        ganttPlan.initGanttDiagram();
        return ganttPlan;
    }

    private Node createTableView() {
        VBox.setVgrow(tableView, Priority.ALWAYS);
        tableView.setFixedCellSize(ROW_HEIGHT);
        tableView.createTable();
        tableView.bindScrollsToController();

        ScrollBar horizontalScrollBar = new ScrollBar();
        horizontalScrollBar.setOrientation(Orientation.HORIZONTAL);
        horizontalScrollBar.visibleAmountProperty().bind(tableView.widthProperty());
        horizontalScrollBar.valueProperty().bindBidirectional(controller.taskHorizontalScrollValueProperty());
        return new VBox(tableView, horizontalScrollBar);
    }

    private AbstractButtonsBox createButtonsBox() {
        buttonBox.createButtonsBox();
        return buttonBox;
    }
}
