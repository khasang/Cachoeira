package ru.khasang.cachoeira.view;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.view.resourcepaneganttchart.ResourcePaneDateLine;
import ru.khasang.cachoeira.view.resourcepaneganttchart.ResourcePaneGridLayer;
import ru.khasang.cachoeira.view.resourcepaneganttchart.ResourcePaneObjectsLayer;

/**
 * Created by truesik on 08.12.2015.
 */
public class ResourceGanttChart extends VBox {
    private ResourcePaneGridLayer resourcePaneGridLayer;        //слой с сеткой
    private ResourcePaneObjectsLayer resourcePaneObjectsLayer;  //слой с объектами диаграммы (задачи, группы, ...)
    private ResourcePaneDateLine resourcePaneDateLine;          //шкала с датами
    private int columnWidth;            //ширина колонок
    private UIControl uiControl;

    public ResourceGanttChart(UIControl uiControl,
                              int columnWidth) {
        this.columnWidth = columnWidth;
        this.uiControl = uiControl;

        resourcePaneGridLayer = new ResourcePaneGridLayer(columnWidth);
        resourcePaneObjectsLayer = new ResourcePaneObjectsLayer(columnWidth);
        resourcePaneObjectsLayer.setUIControl(uiControl);
        ScrollPane verticalScrollPane = new ScrollPane(resourcePaneObjectsLayer);
        verticalScrollPane.setFitToWidth(true);
        verticalScrollPane.getStylesheets().add(this.getClass().getResource("/css/scrollpane.css").toExternalForm()); //делаем вертикальный скроллпэйн прозрачным

        StackPane diagramPane = new StackPane(resourcePaneGridLayer, verticalScrollPane); //нижний слой сетка, а над ним располагается слой с задачами
        VBox.setVgrow(diagramPane, Priority.ALWAYS);

        resourcePaneDateLine = new ResourcePaneDateLine(columnWidth);
        resourcePaneDateLine.setUIControl(uiControl);
        resourcePaneDateLine.initDateLine(uiControl.getController().getProject().getStartDate(), uiControl.getController().getProject().getFinishDate());
        resourcePaneDateLine.setListeners(uiControl.getController().getProject().startDateProperty(), uiControl.getController().getProject().finishDateProperty());
        VBox vBox = new VBox(resourcePaneDateLine, diagramPane);
        VBox.setVgrow(resourcePaneDateLine, Priority.NEVER);

        ScrollPane horizontalScrollPane = new ScrollPane(vBox);
        horizontalScrollPane.setFitToHeight(true);
        horizontalScrollPane.getStyleClass().add("edge-to-edge"); //убирает подсветку синюю границу вокруг скроллпэйна
        horizontalScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        horizontalScrollPane.setPannable(true); //дает возможность двигать диаграмму зажав кнопку мышки, но работает только если жать на шкалу времени

        this.getChildren().addAll(horizontalScrollPane);
        VBox.setVgrow(horizontalScrollPane, Priority.ALWAYS);
    }

    public ResourcePaneGridLayer getResourcePaneGridLayer() {
        return resourcePaneGridLayer;
    }

    public ResourcePaneObjectsLayer getResourcePaneObjectsLayer() {
        return resourcePaneObjectsLayer;
    }

    public ResourcePaneDateLine getResourcePaneDateLine() {
        return resourcePaneDateLine;
    }
}