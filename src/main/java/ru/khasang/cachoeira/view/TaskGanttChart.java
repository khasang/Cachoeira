package ru.khasang.cachoeira.view;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.view.taskpaneganttchart.TaskPaneDateLine;
import ru.khasang.cachoeira.view.taskpaneganttchart.TaskPaneGridLayer;
import ru.khasang.cachoeira.view.taskpaneganttchart.TaskPaneObjectsLayer;

/**
 * Класс в котором определяется порядок расстановки слоев диаграммы Ганта.
 */
public class TaskGanttChart extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(TaskGanttChart.class.getName());

    private TaskPaneObjectsLayer taskPaneObjectsLayer;  //слой с объектами диаграммы (задачи, группы, ...)

    public TaskGanttChart() {
    }

    public void initGanttDiagram(UIControl uiControl) {
        this.getChildren().addAll(createGanttDiagram(
                createDateLine(uiControl),
                createGridLayer(uiControl),
                createObjectsLayer(uiControl),
                uiControl));
        logger.debug("Инициализация диаграммы Ганта на вкладке \"Задачи\" прошла успешно.");
    }

    /**
     * Создаем диаграмму из кусков
     *
     * @param dateLine     Передаем параметр со шкалой дат (createDateLine())
     * @param gridLayer    Передаем параметр с сеткой на заднем плане диаграммы (createGridLayer)
     * @param objectsLayer Передаем параметр со слоем на котором находятся объекты (createObjectsLayer)
     * @return Возвращает ScrollPane, который уже добавляем в чилдрены к экземпляру этого класса (this.getChildren.add(createGanttDiagram())
     */
    private ScrollPane createGanttDiagram(TaskPaneDateLine dateLine,
                                          TaskPaneGridLayer gridLayer,
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
        VBox.setVgrow(horizontalScrollPane, Priority.ALWAYS);
        // Связываем горизонтальные скроллы с вкладок Задачи и Ресурсы
        horizontalScrollPane.hvalueProperty().bindBidirectional(uiControl.horizontalScrollProperty());

        logger.debug("Диаграмма создана.");
        return horizontalScrollPane;
    }

    /**
     * Метод создает слой с сеткой
     *
     * @param uiControl Контроллер вьюхи
     */
    private TaskPaneGridLayer createGridLayer(UIControl uiControl) {
        logger.debug("Создан слой с сеткой.");
        return new TaskPaneGridLayer(uiControl);
    }

    /**
     * Метод создает слой для объектов диаграммы
     *
     * @param uiControl Контроллер вьюхи
     */
    private ScrollPane createObjectsLayer(UIControl uiControl) {
        taskPaneObjectsLayer = new TaskPaneObjectsLayer();
        taskPaneObjectsLayer.setUIControl(uiControl);
        taskPaneObjectsLayer.setListeners(uiControl);
        /** Запихиваем слой объектов в скролл пэйн */
        ScrollPane verticalScrollPane = new ScrollPane(taskPaneObjectsLayer);
        verticalScrollPane.setFitToWidth(true);
        verticalScrollPane.getStylesheets().add(this.getClass().getResource("/css/scrollpane.css").toExternalForm()); //делаем вертикальный скроллпэйн прозрачным
        /** Синхронизируем вертикальный скролл слоя объектов cо скроллом таблицы задач */
        verticalScrollPane.vvalueProperty().bindBidirectional(uiControl.taskVerticalScrollProperty());

        logger.debug("Создан слой для объектов диаграммы.");
        return verticalScrollPane;
    }

    /**
     * Метод создает шкалу с датами
     *
     * @param uiControl Контроллер вьюхи
     */
    private TaskPaneDateLine createDateLine(UIControl uiControl) {
        TaskPaneDateLine taskPaneDateLine = new TaskPaneDateLine();
        taskPaneDateLine.setUIControl(uiControl);
        taskPaneDateLine.initDateLine(
                uiControl.getController().getProject().getStartDate(),
                uiControl.getController().getProject().getFinishDate(),
                uiControl.getZoomMultiplier());
        taskPaneDateLine.setListeners(
                uiControl.getController().getProject().startDateProperty(),
                uiControl.getController().getProject().finishDateProperty(),
                uiControl.zoomMultiplierProperty());
        logger.debug("Создана шкала с датами.");
        return taskPaneDateLine;
    }

    public TaskPaneObjectsLayer getTaskPaneObjectsLayer() {
        return taskPaneObjectsLayer;
    }
}
