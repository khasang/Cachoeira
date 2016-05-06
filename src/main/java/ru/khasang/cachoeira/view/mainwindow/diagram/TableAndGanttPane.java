package ru.khasang.cachoeira.view.mainwindow.diagram;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.view.mainwindow.diagram.buttonbox.AbstractButtonsBox;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.AbstractGanttPlan;
import ru.khasang.cachoeira.view.mainwindow.diagram.tables.AbstractTableView;

public abstract class TableAndGanttPane extends VBox {
    private static final double ROW_HEIGHT = 31;

    protected AbstractTableView tableView;
    protected AbstractGanttPlan ganttPlan;
    protected AbstractButtonsBox buttonBox;

    public void createPane() {
        SplitPane splitPane = new SplitPane(createTableView(), createGanttPLan());
        splitPane.setDividerPosition(0, 0.3);
        VBox.setVgrow(splitPane, Priority.ALWAYS);
        // Связываем разделитель таблицы и диаграммы на вкладке Задачи с разделителем на вкладке Ресурсы
//        splitPane.getDividers().get(0).positionProperty().bindBidirectional(uiControl.splitPaneDividerValueProperty());
        HBox hBox = createButtonsBox();
        VBox.setVgrow(hBox, Priority.NEVER);
        this.getChildren().addAll(splitPane, hBox);
    }

    private Node createGanttPLan() {
        ganttPlan.initGanttDiagram();
        return ganttPlan;
    }

    private Node createTableView() {
        VBox.setVgrow(tableView, Priority.ALWAYS);
        tableView.setFixedCellSize(ROW_HEIGHT);

        ScrollBar horizontalScrollBar = new ScrollBar();
        horizontalScrollBar.setOrientation(Orientation.HORIZONTAL);
        horizontalScrollBar.visibleAmountProperty().bind(tableView.widthProperty());
//        horizontalScrollBar.valueProperty().bindBidirectional(uiControl.taskHorizontalScrollValueProperty());
        return new VBox(tableView, horizontalScrollBar);
    }

    private AbstractButtonsBox createButtonsBox() {
        buttonBox.createButtonsBox();
        return buttonBox;
    }
}
