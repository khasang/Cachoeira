package ru.khasang.cachoeira.view;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.view.ganttchart.DateLine;
import ru.khasang.cachoeira.view.ganttchart.GridLayer;
import ru.khasang.cachoeira.view.ganttchart.ObjectsLayer;

/**
 * Created by truesik on 08.11.2015.
 */
public class GanttChart extends VBox {
    private GridLayer gridLayer;        //слой с сеткой
    private ObjectsLayer objectsLayer;  //слой с объектами диаграммы (задачи, группы, ...)
    private DateLine dateLine;          //шкала с датами
    private int columnWidth;            //ширина колонок
    private UIControl uiControl;

    public GanttChart(IController controller, UIControl uiControl, int columnWidth) {
        this.columnWidth = columnWidth;
        this.uiControl = uiControl;

        gridLayer = new GridLayer(columnWidth);
        objectsLayer = new ObjectsLayer(controller, columnWidth);
        objectsLayer.setUIControl(uiControl);
        ScrollPane verticalScrollPane = new ScrollPane(objectsLayer);
        verticalScrollPane.setFitToWidth(true);
        verticalScrollPane.getStylesheets().add(this.getClass().getResource("/css/scrollpane.css").toExternalForm()); //делаем вертикальный скроллпэйн прозрачным

        StackPane diagramPane = new StackPane(gridLayer, verticalScrollPane); //нижний слой сетка, а над ним располагается слой с задачами
        VBox.setVgrow(diagramPane, Priority.ALWAYS);

        dateLine = new DateLine(controller, columnWidth);
        dateLine.setUiControl(uiControl);
        dateLine.initDateLine();
        VBox vBox = new VBox(dateLine, diagramPane);
        VBox.setVgrow(dateLine, Priority.NEVER);

        ScrollPane horizontalScrollPane = new ScrollPane(vBox);
        horizontalScrollPane.setFitToHeight(true);
        horizontalScrollPane.getStyleClass().add("edge-to-edge"); //убирает подсветку синюю границу вокруг скроллпэйна
        horizontalScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        horizontalScrollPane.setPannable(true); //дает возможность двигать диаграмму зажав кнопку мышки, но работает только если жать на шкалу времени

        this.getChildren().addAll(horizontalScrollPane);
        VBox.setVgrow(horizontalScrollPane, Priority.ALWAYS);
    }

    public GridLayer getGridLayer() {
        return gridLayer;
    }

    public ObjectsLayer getObjectsLayer() {
        return objectsLayer;
    }

    public DateLine getDateLine() {
        return dateLine;
    }
}
