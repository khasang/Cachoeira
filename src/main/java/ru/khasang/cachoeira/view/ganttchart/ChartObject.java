package ru.khasang.cachoeira.view.ganttchart;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

import java.time.temporal.ChronoUnit;

/**
 * Created by truesik on 04.11.2015.
 */
public class ChartObject extends HBox {
    private static final double TASK_HEIGHT = 18;   //высота прямоугольника задачи
    private static final double SPACING = 10;       //расстояние между элементами (прямоугольником задачи и лэйблами с названиями ресурсов)
    private static final double rowHeight = 24;

    private IController controller;

    private ITask task;
    private double rowIndex;                        //координата Y (строка задачи)
    private int columnWidth;
    private Rectangle bar;

    public ChartObject(IController controller, ITask task, double rowIndex, int columnWidth) {
        this.controller = controller;
        this.task = task;
        this.rowIndex = rowIndex;
        this.columnWidth = columnWidth;
        setSpacing(SPACING);
        setMinHeight(24);
        setAlignment(Pos.CENTER);

        double startDay = (ChronoUnit.DAYS.between(controller.getProject().getStartDate(), task.getStartDate())) * columnWidth;
        setLayoutX(startDay - 2); //"2" - подгонка под сетку
        setLayoutY(rowIndex * rowHeight);

        double taskWidth = (ChronoUnit.DAYS.between(task.getStartDate(), task.getFinishDate()) * columnWidth);

        bar = new Rectangle(0, 0, taskWidth, TASK_HEIGHT); //создаем прямоугольник
        bar.setFill(Color.valueOf("#03A9F4"));    //цвет прямоугольника
        bar.setStroke(Color.valueOf("#B3E5FC"));  //цвет окантовки
        bar.setArcHeight(5);                      //сгругление углов
        bar.setArcWidth(5);                       //сгругление углов

        //подсветка при наведении
        bar.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                bar.setFill(Color.valueOf("#41C2FA"));
                bar.setStroke(Color.valueOf("#72D1FA"));
            } else {
                bar.setFill(Color.valueOf("#03A9F4"));    //цвет прямоугольника
                bar.setStroke(Color.valueOf("#B3E5FC"));
            }
        });

        getChildren().add(bar); //добавляем прямоугольник на HBox (панель)

        setContextMenu(bar); //контекстное меню при нажатии на прямоугольник, добавлено для теста (код нужно дописать)
    }

    public void showResourcesOnDiagram() {
        this.getChildren().retainAll(bar);
        for (IResource resource : this.task.getResourceList()) {
            Label resourceName = new Label(resource.getName());
            getChildren().add(resourceName); //добавляем ресурсы на HBox
        }
    }

    public void setContextMenu(Rectangle taskShape) {
        Menu setResource = new Menu("Назначить ресурс");
        MenuItem getProperties = new MenuItem("Свойства");
        getProperties.setOnAction(event -> controller.selectedTaskProperty().setValue(task));
        MenuItem removeTask = new MenuItem("Удалить задачу");

        ContextMenu contextMenu = new ContextMenu(setResource, getProperties, removeTask);

        taskShape.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                contextMenu.show(ChartObject.this, event.getScreenX(), event.getScreenY());
            }
        });
    }
}
