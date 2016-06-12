package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.layers;

import ru.khasang.cachoeira.viewcontroller.MainWindowController;

public class GanttChartLayerFactoryImpl implements GanttChartLayerFactory {
    private MainWindowController controller;

    public GanttChartLayerFactoryImpl(MainWindowController controller) {
        this.controller = controller;
    }

    @Override
    public AbstractChartLayer createDateLine() {
        DateLineChartLayer dateLineChartLayer = new DateLineChartLayer(controller);
        dateLineChartLayer.createLayer();
        return dateLineChartLayer;
    }

    @Override
    public AbstractChartLayer createObjectsLayer() {
        ObjectsChartLayer objectsChartLayer = new ObjectsChartLayer();
        objectsChartLayer.createLayer();
        return objectsChartLayer;
    }

    @Override
    public AbstractChartLayer createGridLayer() {
        GridChartLayer gridChartLayer = new GridChartLayer(controller);
        gridChartLayer.createLayer();
        return gridChartLayer;
    }

    @Override
    public AbstractChartLayer createRelationsLayer() {
        throw new RuntimeException("Still not implemented.");
    }
}
