package ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan;

import javafx.scene.control.ScrollPane;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.dateline.DateLine;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.dateline.ResourceGanttPlanDateLine;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.gridlayer.GridLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.gridlayer.ResourceGanttPlanGridLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.labelslayer.TaskBarLabelsLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.ResourceGanttPlanObjectsLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.relationlayer.RelationsLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.selectedobjectlayer.SelectedObjectLayer;
import ru.khasang.cachoeira.viewcontroller.UIControl;

public class ResourceGanttPlan extends GanttPlan {
    public ResourceGanttPlan(MainWindowController controller) {
        this.controller = controller;
    }

    @Override
    protected GridLayer createGridLayer() {
        return new ResourceGanttPlanGridLayer(controller);
    }

    @Override
    protected ScrollPane createObjectsLayer() {
        objectsLayer = new ResourceGanttPlanObjectsLayer(controller);
        // Запихиваем слой объектов в скролл пэйн
        ScrollPane verticalScrollPane = new ScrollPane(objectsLayer);
        verticalScrollPane.setFitToWidth(true);
        verticalScrollPane.getStylesheets().add(this.getClass().getResource("/css/scrollpane.css").toExternalForm()); //делаем вертикальный скроллпэйн прозрачным
        // Синхронизируем вертикальный скролл слоя объектов cо скроллом таблицы задач
        verticalScrollPane.vvalueProperty().bindBidirectional(controller.resourceVerticalScrollValueProperty());
        return verticalScrollPane;
    }

    @Override
    protected DateLine createDateLine() {
        DateLine dateLine = new ResourceGanttPlanDateLine(controller);
        dateLine.initDateLine(
                controller.getProject().getStartDate(),
                controller.getProject().getFinishDate(),
                controller.getZoomMultiplier());
        dateLine.setListeners(
                controller.getProject().startDateProperty(),
                controller.getProject().finishDateProperty(),
                controller.zoomMultiplierProperty());
        return dateLine;
    }

    @Override
    public RelationsLayer getRelationsLayer() {
        throw new RuntimeException("Not Supported");
    }

    @Override
    public TaskBarLabelsLayer getLabelsLayer() {
        throw new RuntimeException("Not Supported");
    }

    @Override
    public SelectedObjectLayer getSelectedObjectLayer() {
        throw new RuntimeException("Not Supported");
    }
}
