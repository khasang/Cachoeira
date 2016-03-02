package ru.khasang.cachoeira.view;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.view.resourcepaneganttchart.ResourcePaneDateLine;
import ru.khasang.cachoeira.view.resourcepaneganttchart.ResourcePaneGridLayer;
import ru.khasang.cachoeira.view.resourcepaneganttchart.ResourcePaneObjectsLayer;

/**
 * Класс в котором определяется порядок расстановки слоев диаграммы Ганта.
 */
public class ResourceGanttChart extends VBox {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceGanttChart.class.getName());

    private ResourcePaneObjectsLayer resourcePaneObjectsLayer;  //слой с объектами диаграммы (задачи, группы, ...)

    public ResourceGanttChart() {
    }

    public void initGanttDiagram(UIControl uiControl) {
        this.getChildren().addAll(createGanttDiagram(
                createDateLine(uiControl),
                createGridLayer(uiControl),
                createObjectsLayer(uiControl),
                uiControl));
        LOGGER.debug("Инициализация диаграммы Ганта на вкладке \"Ресурсы\" прошла успешно.");
    }

    /**
     * Создаем диаграмму из кусков
     *
     * @param dateLine     Передаем параметр со шкалой дат (createDateLine())
     * @param gridLayer    Передаем параметр с сеткой на заднем плане диаграммы (createGridLayer)
     * @param objectsLayer Передаем параметр со слоем на котором находятся объекты (createObjectsLayer)
     * @return Возвращает ScrollPane, который уже добавляем в чилдрены к экземпляру этого класса (this.getChildren.add(createGanttDiagram())
     */
    private ScrollPane createGanttDiagram(ResourcePaneDateLine dateLine,
                                          ResourcePaneGridLayer gridLayer,
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

        LOGGER.debug("Диаграмма создана.");
        return horizontalScrollPane;
    }

    /**
     * Метод создает слой для объектов диаграммы
     *
     * @param uiControl Контроллер вьюхи
     */
    private ScrollPane createObjectsLayer(UIControl uiControl) {
        resourcePaneObjectsLayer = new ResourcePaneObjectsLayer();
        resourcePaneObjectsLayer.setUIControl(uiControl);
//        resourcePaneObjectsLayer.setListeners(uiControl);
        // Запихиваем слой объектов в скролл пэйн
        ScrollPane verticalScrollPane = new ScrollPane(resourcePaneObjectsLayer);
        verticalScrollPane.setFitToWidth(true);
        verticalScrollPane.getStylesheets().add(this.getClass().getResource("/css/scrollpane.css").toExternalForm()); //делаем вертикальный скроллпэйн прозрачным
        // Синхронизируем вертикальный скролл слоя объектов cо скроллом таблицы задач
        verticalScrollPane.vvalueProperty().bindBidirectional(uiControl.resourceVerticalScrollValueProperty());

        LOGGER.debug("Создан слой для объектов диаграммы.");
        return verticalScrollPane;
    }

    /**
     * Метод создает слой с сеткой
     *
     * @param uiControl Контроллер вьюхи
     */
    private ResourcePaneGridLayer createGridLayer(UIControl uiControl) {
        LOGGER.debug("Создан слой с сеткой.");
        return new ResourcePaneGridLayer(uiControl);
    }

    /**
     * Метод создает шкалу с датами
     *
     * @param uiControl Контроллер вьюхи
     */
    private ResourcePaneDateLine createDateLine(UIControl uiControl) {
        ResourcePaneDateLine resourcePaneDateLine = new ResourcePaneDateLine();
        resourcePaneDateLine.setUIControl(uiControl);
        resourcePaneDateLine.initDateLine(
                uiControl.getController().getProject().getStartDate(),
                uiControl.getController().getProject().getFinishDate(),
                uiControl.getZoomMultiplier());
        resourcePaneDateLine.setListeners(
                uiControl.getController().getProject().startDateProperty(),
                uiControl.getController().getProject().finishDateProperty(),
                uiControl.zoomMultiplierProperty());
        LOGGER.debug("Создана шкала с датами.");
        return resourcePaneDateLine;
    }

    public ResourcePaneObjectsLayer getResourcePaneObjectsLayer() {
        return resourcePaneObjectsLayer;
    }
}