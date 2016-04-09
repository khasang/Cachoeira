package ru.khasang.cachoeira.view.mainwindow.ganttplan;

import javafx.scene.control.ScrollPane;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.dateline.DateLine;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.dateline.ResourceGanttPlanDateLine;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.gridlayer.GridLayer;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.gridlayer.ResourceGanttPlanGridLayer;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.labelslayer.TaskBarLabelsLayer;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.objectslayer.ResourceGanttPlanObjectsLayer;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.relationlayer.RelationsLayer;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.selectedobjectlayer.SelectedObjectLayer;

public class ResourceGanttPlan extends GanttPlan {
    @Override
    protected GridLayer createGridLayer(UIControl uiControl) {
        return new ResourceGanttPlanGridLayer(uiControl);
    }

    @Override
    protected ScrollPane createObjectsLayer(UIControl uiControl) {
        objectsLayer = new ResourceGanttPlanObjectsLayer();
        objectsLayer.setUIControl(uiControl);
        // Запихиваем слой объектов в скролл пэйн
        ScrollPane verticalScrollPane = new ScrollPane(objectsLayer);
        verticalScrollPane.setFitToWidth(true);
        verticalScrollPane.getStylesheets().add(this.getClass().getResource("/css/scrollpane.css").toExternalForm()); //делаем вертикальный скроллпэйн прозрачным
        // Синхронизируем вертикальный скролл слоя объектов cо скроллом таблицы задач
        verticalScrollPane.vvalueProperty().bindBidirectional(uiControl.resourceVerticalScrollValueProperty());
        return verticalScrollPane;
    }

    @Override
    protected DateLine createDateLine(UIControl uiControl) {
        DateLine dateLine = new ResourceGanttPlanDateLine();
        dateLine.setUIControl(uiControl);
        dateLine.initDateLine(
                uiControl.getController().getProject().getStartDate(),
                uiControl.getController().getProject().getFinishDate(),
                uiControl.getZoomMultiplier());
        dateLine.setListeners(
                uiControl.getController().getProject().startDateProperty(),
                uiControl.getController().getProject().finishDateProperty(),
                uiControl.zoomMultiplierProperty());
        return dateLine;
    }

    @Override
    public RelationsLayer getRelationsLayer() {
        return null;
    }

    @Override
    public TaskBarLabelsLayer getLabelsLayer() {
        return null;
    }

    @Override
    public SelectedObjectLayer getSelectedObjectLayer() {
        return null;
    }
}
