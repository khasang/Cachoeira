package ru.khasang.cachoeira.view.ganttplan;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.ganttplan.dateline.DateLine;
import ru.khasang.cachoeira.view.ganttplan.gridlayer.GridLayer;
import ru.khasang.cachoeira.view.ganttplan.objectslayer.ObjectsLayer;

public abstract class GanttPlan extends VBox {
    protected ObjectsLayer objectsLayer;

    public GanttPlan() {
    }

    public void initGanttDiagram(UIControl uiControl) {
        this.getChildren().addAll(createGanttPlan(
                createDateLine(uiControl),
                createGridLayer(uiControl),
                createObjectsLayer(uiControl),
                uiControl));
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
                                       ScrollPane objectsLayer,
                                       UIControl uiControl) {
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
        horizontalScrollPane.hvalueProperty().bindBidirectional(uiControl.horizontalScrollValueProperty());

        return horizontalScrollPane;
    }

    /**
     * Метод создает слой с сеткой
     *
     * @param uiControl Контроллер вьюхи
     */
    protected abstract GridLayer createGridLayer(UIControl uiControl);

    /**
     * Метод создает слой для объектов диаграммы
     *
     * @param uiControl Контроллер вьюхи
     */
    protected abstract ScrollPane createObjectsLayer(UIControl uiControl);

    /**
     * Метод создает шкалу с датами
     *
     * @param uiControl Контроллер вьюхи
     */
    protected abstract DateLine createDateLine(UIControl uiControl);

    public ObjectsLayer getObjectsLayer() {
        return objectsLayer;
    }

//    abstract RelationsLayer getRelationsLayer();
//
//    abstract LabelLayer getLabelLayer();
//
//    abstract SelectedObjectLayer getSelectedObjectLayer();
}
