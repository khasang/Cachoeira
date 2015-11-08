package ru.khasang.cachoeira.view;

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

    public GanttChart(IController controller, UIControl UIControl, MainWindow mainWindow) {
        ganttChartGridLayer = new GanttChartGridLayer();
        ganttChartObjectsLayer = new GanttChartObjectsLayer(controller, UIControl, mainWindow);
        StackPane stackPane = new StackPane(ganttChartGridLayer, ganttChartObjectsLayer); //нижний слой сетка, а над ним располагается слой с задачами
        VBox.setVgrow(stackPane, Priority.ALWAYS);

        ganttChartDateLine = new GanttChartDateLine();
        this.getChildren().addAll(ganttChartDateLine, stackPane);
        VBox.setVgrow(ganttChartDateLine, Priority.NEVER);
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
