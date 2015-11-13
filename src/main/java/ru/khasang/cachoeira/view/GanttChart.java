package ru.khasang.cachoeira.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.controller.IController;

/**
 * Created by truesik on 08.11.2015.
 */
public class GanttChart extends VBox {
    private GanttChartGridLayer ganttChartGridLayer;        //слой с сеткой
    private GanttChartObjectsLayer ganttChartObjectsLayer;  //слой с объектами диаграммы (задачи, группы, ...)
    private GanttChartDateLine ganttChartDateLine;          //шкала с датами
    private int columnWidth;                                //ширина колонок

    public GanttChart(IController controller, UIControl UIControl, MainWindow mainWindow, int columnWidth) {
        this.columnWidth = columnWidth;

        ganttChartGridLayer = new GanttChartGridLayer(columnWidth);
        ganttChartObjectsLayer = new GanttChartObjectsLayer(controller, UIControl, mainWindow, columnWidth);
        ScrollPane verticalScrollPane = new ScrollPane(ganttChartObjectsLayer);
        verticalScrollPane.setFitToWidth(true);
        verticalScrollPane.getStylesheets().add(this.getClass().getResource("/css/scrollpane.css").toExternalForm()); //делаем вертикальный скроллпэйн прозрачным

        //попытка синхронизировать скролл от таблицы и диаграммы (работает хреново)
        verticalScrollPane.setVmax(10); //10 - рандомное число, по идее должно быть равно количеству строк в таблице
        verticalScrollPane.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mainWindow.getTaskTreeTableView().scrollTo(newValue.intValue()); //говорим таблице куда скроллить
            }
        });

        StackPane diagramPane = new StackPane(ganttChartGridLayer, verticalScrollPane); //нижний слой сетка, а над ним располагается слой с задачами
        VBox.setVgrow(diagramPane, Priority.ALWAYS);

        ganttChartDateLine = new GanttChartDateLine(controller, columnWidth);
        VBox vBox = new VBox(ganttChartDateLine, diagramPane);
        VBox.setVgrow(ganttChartDateLine, Priority.NEVER);

        ScrollPane horizontalScrollPane = new ScrollPane(vBox);
        horizontalScrollPane.setFitToHeight(true);
        horizontalScrollPane.getStyleClass().add("edge-to-edge"); //убирает подсветку синюю границу вокруг скроллпэйна
        horizontalScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        horizontalScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        horizontalScrollPane.setPannable(true); //дает возможность двигать диаграмму зажав кнопку мышки, но работает только если жать на шкалу времени

        this.getChildren().addAll(horizontalScrollPane);
        VBox.setVgrow(horizontalScrollPane, Priority.ALWAYS);
    }

    public GanttChartGridLayer getGanttChartGridLayer() {
        return ganttChartGridLayer;
    }

    public GanttChartObjectsLayer getGanttChartObjectsLayer() {
        return ganttChartObjectsLayer;
    }

    public GanttChartDateLine getGanttChartDateLine() {
        return ganttChartDateLine;
    }
}
