package ru.khasang.cachoeira.view.ganttplan;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.ganttplan.dateline.DateLine;
import ru.khasang.cachoeira.view.ganttplan.dateline.TaskGanttPlanDateLine;
import ru.khasang.cachoeira.view.ganttplan.gridlayer.GridLayer;
import ru.khasang.cachoeira.view.ganttplan.gridlayer.TaskGanttPlanGridLayer;
import ru.khasang.cachoeira.view.ganttplan.objectslayer.TaskGanttPlanObjectsLayer;

public class TaskGanttPlan extends GanttPlan {
    @Override
    protected GridLayer createGridLayer(UIControl uiControl) {
        return new TaskGanttPlanGridLayer(uiControl);
    }

    @Override
    protected ScrollPane createObjectsLayer(UIControl uiControl) {
        // Слой на котором отображаются метки задач
        objectsLayer = new TaskGanttPlanObjectsLayer();
        objectsLayer.setUIControl(uiControl);
        StackPane stackPane = new StackPane(objectsLayer);
        // Запихиваем слой объектов в скролл пэйн
        ScrollPane verticalScrollPane = new ScrollPane(stackPane);
        verticalScrollPane.setFitToWidth(true);
        verticalScrollPane.getStylesheets().add(this.getClass().getResource("/css/scrollpane.css").toExternalForm()); //делаем вертикальный скроллпэйн прозрачным
        // Синхронизируем вертикальный скролл слоя объектов cо скроллом таблицы задач
        verticalScrollPane.vvalueProperty().bindBidirectional(uiControl.taskVerticalScrollValueProperty());

        return verticalScrollPane;
    }

    @Override
    protected DateLine createDateLine(UIControl uiControl) {
        DateLine dateLine = new TaskGanttPlanDateLine();
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
}
