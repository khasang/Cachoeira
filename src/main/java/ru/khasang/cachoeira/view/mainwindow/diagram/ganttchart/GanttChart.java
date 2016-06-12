package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.layers.AbstractChartLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.layers.DateLineChartLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.layers.GanttChartLayerFactory;

public abstract class GanttChart extends VBox {
    private final AbstractChartLayer dataLineChartLayer;
    private final AbstractChartLayer gridChartLayer;
    private final AbstractChartLayer objectsChartLayer;

    private ScrollPane horizontalScrollPane;
    private ScrollPane verticalScrollPane;

    /**
     * Constructor.
     *
     * @param layerFactory Takes layer factory.
     */
    public GanttChart(GanttChartLayerFactory layerFactory) {
        this.dataLineChartLayer = layerFactory.createDateLine();
        this.gridChartLayer = layerFactory.createGridLayer();
        this.objectsChartLayer = layerFactory.createObjectsLayer();
    }

    /**
     * Creates Gantt chart.
     */
    public void createChart() {
        this.getChildren().add(
                createChart(
                        dataLineChartLayer,
                        gridChartLayer,
                        objectsChartLayer));
    }

    /**
     * Overloaded method that creates Gantt chart.
     *
     * @param dataLineChartLayer Date line layer.
     * @param gridChartLayer     Background grid layer.
     * @param objectsChartLayer  Layer that displays task bars.
     * @return Returns Gantt chart.
     */
    private Node createChart(AbstractChartLayer dataLineChartLayer,
                             AbstractChartLayer gridChartLayer,
                             AbstractChartLayer objectsChartLayer) {
        StackPane stackPane = new StackPane((Node) gridChartLayer, (Node) objectsChartLayer);
        verticalScrollPane = new ScrollPane(stackPane);
        verticalScrollPane.setFitToWidth(true);
        verticalScrollPane.setFitToHeight(true);
        verticalScrollPane.getStyleClass().add("edge-to-edge");
        VBox.setVgrow(verticalScrollPane, Priority.ALWAYS);

        VBox vBox = new VBox((DateLineChartLayer) dataLineChartLayer, verticalScrollPane);
        VBox.setVgrow((DateLineChartLayer) dataLineChartLayer, Priority.NEVER);

        horizontalScrollPane = new ScrollPane(vBox);
        horizontalScrollPane.setFitToHeight(true);
        horizontalScrollPane.getStyleClass().add("edge-to-edge");
        horizontalScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        horizontalScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        VBox.setVgrow(horizontalScrollPane, Priority.ALWAYS);

        return horizontalScrollPane;
    }

    /**
     * Getter.
     *
     * @return Returns horizontal scroll pane.
     */
    public ScrollPane getHorizontalScrollPane() {
        return horizontalScrollPane;
    }

    /**
     * Getter.
     *
     * @return Returns vertical scroll pane.
     */
    public ScrollPane getVerticalScrollPane() {
        return verticalScrollPane;
    }

    /**
     * Getter.
     *
     * @return Returns data line.
     */
    public AbstractChartLayer getDataLineChartLayer() {
        return dataLineChartLayer;
    }

    /**
     * Getter.
     *
     * @return Returns grid layer.
     */
    public AbstractChartLayer getGridChartLayer() {
        return gridChartLayer;
    }

    /**
     * Getter.
     *
     * @return Return objects layer.
     */
    public AbstractChartLayer getObjectsChartLayer() {
        return objectsChartLayer;
    }
}
