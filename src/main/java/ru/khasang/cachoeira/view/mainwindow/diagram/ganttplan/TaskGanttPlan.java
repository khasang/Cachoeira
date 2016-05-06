package ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.dateline.DateLine;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.dateline.TaskGanttPlanDateLine;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.gridlayer.GridLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.gridlayer.TaskGanttPlanGridLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.labelslayer.TaskBarLabelsLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.labelslayer.TaskGanttPlanTaskBarLabelsLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.TaskGanttPlanObjectsLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.relationlayer.RelationsLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.relationlayer.TaskGanttPlanRelationsLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.selectedobjectlayer.SelectedObjectLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.selectedobjectlayer.TaskGanttPlanSelectedObjectLayer;

public class TaskGanttPlan extends AbstractGanttPlan {
    private RelationsLayer relationsLayer;
    private TaskBarLabelsLayer labelsLayer;
    private SelectedObjectLayer selectedObjectLayer;

    public TaskGanttPlan(MainWindowController controller) {
        this.controller = controller;
    }

    @Override
    protected GridLayer createGridLayer() {
        return new TaskGanttPlanGridLayer(controller);
    }

    @Override
    protected ScrollPane createObjectsLayer() {
        // Слой на котором отображаются метки задач
        objectsLayer = new TaskGanttPlanObjectsLayer(controller);

        // Слой на котором отображаются связи между задачами
        relationsLayer = new TaskGanttPlanRelationsLayer();
        relationsLayer.setListeners(controller);

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
        verticalScrollPane.vvalueProperty().bindBidirectional(controller.taskVerticalScrollValueProperty());

        return verticalScrollPane;
    }

    @Override
    protected DateLine createDateLine() {
        DateLine dateLine = new TaskGanttPlanDateLine(controller);
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
