package ru.khasang.cachoeira.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private int columnWidth;                                //ширина колонок

    public GanttChart(IController controller, UIControl uiControl, int columnWidth) {
        this.columnWidth = columnWidth;

        gridLayer = new GridLayer(columnWidth);
        objectsLayer = new ObjectsLayer(controller, uiControl, columnWidth);
        ScrollPane verticalScrollPane = new ScrollPane(objectsLayer);
        verticalScrollPane.setFitToWidth(true);
        verticalScrollPane.getStylesheets().add(this.getClass().getResource("/css/scrollpane.css").toExternalForm()); //делаем вертикальный скроллпэйн прозрачным

        //попытка синхронизировать скролл от таблицы и диаграммы (работает хреново)
        verticalScrollPane.setVmax(10); //10 - рандомное число, по идее должно быть равно количеству строк в таблице
        verticalScrollPane.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                uiControl.getMainWindow().getTaskTreeTableView().scrollTo(newValue.intValue()); //говорим таблице куда скроллить // TODO: 25.11.2015 fix it
            }
        });

        StackPane diagramPane = new StackPane(gridLayer, verticalScrollPane); //нижний слой сетка, а над ним располагается слой с задачами
        VBox.setVgrow(diagramPane, Priority.ALWAYS);

        dateLine = new DateLine(controller, columnWidth);
        VBox vBox = new VBox(dateLine, diagramPane);
        VBox.setVgrow(dateLine, Priority.NEVER);

        ScrollPane horizontalScrollPane = new ScrollPane(vBox);
        horizontalScrollPane.setFitToHeight(true);
        horizontalScrollPane.getStyleClass().add("edge-to-edge"); //убирает подсветку синюю границу вокруг скроллпэйна
        horizontalScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        horizontalScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
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
