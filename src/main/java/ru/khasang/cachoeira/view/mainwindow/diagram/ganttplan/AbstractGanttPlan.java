package ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.dateline.DateLine;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.gridlayer.GridLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.labelslayer.TaskBarLabelsLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.ObjectsLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.relationlayer.RelationsLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.selectedobjectlayer.SelectedObjectLayer;

public abstract class AbstractGanttPlan extends VBox {
    protected ObjectsLayer objectsLayer;
    protected MainWindowController controller;

    public void initGanttDiagram() {
        this.getChildren().addAll(createGanttPlan(
                createDateLine(),
                createGridLayer(),
                createObjectsLayer()));
    }

    /**
     * Создаем диаграмму из кусков
     *
     * @param dateLine     Передаем параметр со шкалой дат (createDateLine())
     * @param gridLayer    Передаем параметр с сеткой на заднем плане диаграммы (createGridLayer)
     * @param objectsLayer Передаем параметр со слоем на котором находятся объекты (createObjectsLayer)
     * @return Возвращает ScrollPane, который уже добавляем в чилдрены к экземпляру этого класса (this.getChildren.add(createGanttPlan())
     */
    private ScrollPane createGanttPlan(DateLine dateLine,
                                       GridLayer gridLayer,
                                       ScrollPane objectsLayer) {
        StackPane stackPane = new StackPane(gridLayer, objectsLayer);
        VBox.setVgrow(stackPane, Priority.ALWAYS);
        VBox vBox = new VBox(dateLine, stackPane);
        VBox.setVgrow(dateLine, Priority.NEVER);

        ScrollPane horizontalScrollPane = new ScrollPane(vBox);
        horizontalScrollPane.setFitToHeight(true);
        horizontalScrollPane.getStyleClass().add("edge-to-edge"); //убирает синюю границу вокруг скроллпэйна
        horizontalScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        horizontalScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        VBox.setVgrow(horizontalScrollPane, Priority.ALWAYS);
        // Связываем горизонтальные скроллы с вкладок Задачи и Ресурсы
        horizontalScrollPane.hvalueProperty().bindBidirectional(controller.ganttHorizontalScrollValueProperty());

        return horizontalScrollPane;
    }

    /**
     * Метод создает слой с сеткой
     */
    protected abstract GridLayer createGridLayer();

    /**
     * Метод создает слой для объектов диаграммы
     */
    protected abstract ScrollPane createObjectsLayer();

    /**
     * Метод создает шкалу с датами
     */
    protected abstract DateLine createDateLine();

    public ObjectsLayer getObjectsLayer() {
        return objectsLayer;
    }

    public abstract RelationsLayer getRelationsLayer();

    public abstract TaskBarLabelsLayer getLabelsLayer();

    public abstract SelectedObjectLayer getSelectedObjectLayer();
}
