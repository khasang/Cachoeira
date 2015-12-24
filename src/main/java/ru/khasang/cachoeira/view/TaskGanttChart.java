package ru.khasang.cachoeira.view;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.view.taskpaneganttchart.TaskPaneDateLine;
import ru.khasang.cachoeira.view.taskpaneganttchart.TaskPaneGridLayer;
import ru.khasang.cachoeira.view.taskpaneganttchart.TaskPaneObjectsLayer;

/**
 * Created by truesik on 08.11.2015.
 */
public class TaskGanttChart extends VBox {
    private TaskPaneGridLayer taskPaneGridLayer;        //слой с сеткой
    private TaskPaneObjectsLayer taskPaneObjectsLayer;  //слой с объектами диаграммы (задачи, группы, ...)
    private TaskPaneDateLine taskPaneDateLine;          //шкала с датами
    private UIControl uiControl;

    public TaskGanttChart(UIControl uiControl) {
        this.uiControl = uiControl;

        taskPaneGridLayer = new TaskPaneGridLayer(uiControl);
        taskPaneObjectsLayer = new TaskPaneObjectsLayer();
        taskPaneObjectsLayer.setUIControl(uiControl);
        ScrollPane verticalScrollPane = new ScrollPane(taskPaneObjectsLayer);
        verticalScrollPane.setFitToWidth(true);
        verticalScrollPane.getStylesheets().add(this.getClass().getResource("/css/scrollpane.css").toExternalForm()); //делаем вертикальный скроллпэйн прозрачным

        StackPane diagramPane = new StackPane(taskPaneGridLayer, verticalScrollPane); //нижний слой сетка, а над ним располагается слой с задачами
        VBox.setVgrow(diagramPane, Priority.ALWAYS);

        taskPaneDateLine = new TaskPaneDateLine();
        taskPaneDateLine.setUIControl(uiControl);
        taskPaneDateLine.initDateLine(uiControl.getController().getProject().getStartDate(), uiControl.getController().getProject().getFinishDate(), uiControl.getZoomMultiplier());
        taskPaneDateLine.setListeners(uiControl.getController().getProject().startDateProperty(), uiControl.getController().getProject().finishDateProperty());
        VBox vBox = new VBox(taskPaneDateLine, diagramPane);
        VBox.setVgrow(taskPaneDateLine, Priority.NEVER);

        ScrollPane horizontalScrollPane = new ScrollPane(vBox);
        horizontalScrollPane.setFitToHeight(true);
        horizontalScrollPane.getStyleClass().add("edge-to-edge"); //убирает подсветку синюю границу вокруг скроллпэйна
        horizontalScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        horizontalScrollPane.setPannable(true); //дает возможность двигать диаграмму зажав кнопку мышки, но работает только если жать на шкалу времени

        this.getChildren().addAll(horizontalScrollPane);
        VBox.setVgrow(horizontalScrollPane, Priority.ALWAYS);
    }

    public TaskPaneGridLayer getTaskPaneGridLayer() {
        return taskPaneGridLayer;
    }

    public TaskPaneObjectsLayer getTaskPaneObjectsLayer() {
        return taskPaneObjectsLayer;
    }

    public TaskPaneDateLine getTaskPaneDateLine() {
        return taskPaneDateLine;
    }
}
