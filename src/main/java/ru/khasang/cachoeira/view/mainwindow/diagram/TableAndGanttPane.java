package ru.khasang.cachoeira.view.mainwindow.diagram;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.view.MaterialButton;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.GanttPlan;
import ru.khasang.cachoeira.view.mainwindow.diagram.tables.AbstractTableView;

public abstract class TableAndGanttPane extends VBox {
    private static final double ROW_HEIGHT = 31;

    protected AbstractTableView tableView;
    protected GanttPlan ganttPlan;

    protected MaterialButton addButton;
    protected MaterialButton removeButton;
    protected Slider zoomSlider;

    public void createPane() {
        SplitPane splitPane = new SplitPane(createTableView(), ganttPlan);
        splitPane.setDividerPosition(0, 0.3);
        VBox.setVgrow(splitPane, Priority.ALWAYS);
        // Связываем разделитель таблицы и диаграммы на вкладке Задачи с разделителем на вкладке Ресурсы
//        splitPane.getDividers().get(0).positionProperty().bindBidirectional(uiControl.splitPaneDividerValueProperty());
        HBox hBox = createButtonsBox();
        VBox.setVgrow(hBox, Priority.NEVER);
        this.getChildren().addAll(splitPane, hBox);
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

    private HBox createButtonsBox() {
        addButton = new MaterialButton("", new ImageView(getClass().getResource("/img/ic_add.png").toExternalForm()));
        removeButton = new MaterialButton("", new ImageView(getClass().getResource("/img/ic_remove.png").toExternalForm()));
        Region separateRegion = new Region();
        HBox.setHgrow(separateRegion, Priority.ALWAYS);
        zoomSlider = new Slider();
        return new HBox(addButton, removeButton, separateRegion, zoomSlider);
    }
}
