package ru.khasang.cachoeira.view.mainwindow.ganttplan;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.dateline.DateLine;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.dateline.TaskGanttPlanDateLine;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.gridlayer.GridLayer;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.gridlayer.TaskGanttPlanGridLayer;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.labelslayer.TaskBarLabelsLayer;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.labelslayer.TaskGanttPlanTaskBarLabelsLayer;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.objectslayer.TaskGanttPlanObjectsLayer;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.relationlayer.RelationsLayer;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.relationlayer.TaskGanttPlanRelationsLayer;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.selectedobjectlayer.SelectedObjectLayer;
import ru.khasang.cachoeira.view.mainwindow.ganttplan.selectedobjectlayer.TaskGanttPlanSelectedObjectLayer;

public class TaskGanttPlan extends GanttPlan {
    private RelationsLayer relationsLayer;
    private TaskBarLabelsLayer labelsLayer;
    private SelectedObjectLayer selectedObjectLayer;

    @Override
    protected GridLayer createGridLayer(UIControl uiControl) {
        return new TaskGanttPlanGridLayer(uiControl);
    }

    @Override
    protected ScrollPane createObjectsLayer(UIControl uiControl) {
        // Слой на котором отображаются метки задач
        objectsLayer = new TaskGanttPlanObjectsLayer();
        objectsLayer.setUIControl(uiControl);

        // Слой на котором отображаются связи между задачами
        relationsLayer = new TaskGanttPlanRelationsLayer();
        relationsLayer.setListeners(uiControl);

        // Слой на котором отображаются ресурсы привязанные к задачам
        labelsLayer = new TaskGanttPlanTaskBarLabelsLayer();

        // Слой на котором отображается выделенная в данный момент задача
        selectedObjectLayer = new TaskGanttPlanSelectedObjectLayer();

        StackPane stackPane = new StackPane(selectedObjectLayer, labelsLayer, relationsLayer, objectsLayer);
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

    @Override
    public RelationsLayer getRelationsLayer() {
        return relationsLayer;
    }

    @Override
    public TaskBarLabelsLayer getLabelsLayer() {
        return labelsLayer;
    }

    @Override
    public SelectedObjectLayer getSelectedObjectLayer() {
        return selectedObjectLayer;
    }
}
