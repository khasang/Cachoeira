package ru.khasang.cachoeira.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

/**
 * Created by truesik on 04.11.2015.
 */
public class GanttChartObject extends HBox {
    private static final double TASK_HEIGHT = 18;   //высота прямоугольника задачи
    private static final double SPACING = 10;       //расстояние между элементами (прямоугольником задачи и лэйблами с названиями ресурсов)
    private static final double rowHeight = 24;
    private IController controller;
    private UIControl UIControl;
    private MainWindow mainWindow;
    private ITask task;
    private double rowIndex;                        //координата Y (строка задачи)
    private int columnWidth;

    public GanttChartObject(IController controller, UIControl UIControl, MainWindow mainWindow, ITask task, double rowIndex, int columnWidth) {
        this.controller = controller;
        this.UIControl = UIControl;
        this.mainWindow = mainWindow;
        this.task = task;
        this.rowIndex = rowIndex;
        this.columnWidth = columnWidth;
        setSpacing(SPACING);
        setMinHeight(24);
        setAlignment(Pos.CENTER);


        double startDay = ((task.getStartDate().getTime() - controller.getProject().getStartDate().getTime()) / (24 * 60 * 60 * 1000)) * columnWidth; //координата Х (дата начала задачи минус дата начала проекта получаем разницу в днях и умножаем ее на длину дня в пикселях(MULTIPLIER)
        setLayoutX(startDay - 2); //"2" - подгонка под сетку
        setLayoutY(rowIndex * rowHeight);

        double taskWidth = ((task.getFinishDate().getTime() - task.getStartDate().getTime()) / (24 * 60 * 60 * 1000)) * columnWidth; // длина прямоугольника (разница между

        Rectangle bar = new Rectangle(0, 0, taskWidth, TASK_HEIGHT); //создаем прямоугольник
        bar.setFill(Color.valueOf("#03A9F4"));    //цвет прямоугольника
        bar.setStroke(Color.valueOf("#B3E5FC"));  //цвет окантовки
        bar.setArcHeight(5);                      //сгругление углов
        bar.setArcWidth(5);                       //сгругление углов
        //подсветка при наведении
        bar.hoverProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    bar.setFill(Color.valueOf("#41C2FA"));
                    bar.setStroke(Color.valueOf("#72D1FA"));
                } else {
                    bar.setFill(Color.valueOf("#03A9F4"));    //цвет прямоугольника
                    bar.setStroke(Color.valueOf("#B3E5FC"));
                }
            }
        });

        getChildren().add(bar); //добавляем прямоугольник на HBox (панель)

        showResourcesOnDiagram(task);

        setContextMenu(bar); //контекстное меню при нажатии на прямоугольник, добавлено для теста (код нужно дописать)
    }

    public void showResourcesOnDiagram(ITask task) {
        for (IResource resource : task.getResourceList()) {
            Label resourceName = new Label(resource.getName());
            getChildren().add(resourceName); //добавляем ресурсы на HBox
        }
    }

    public void setContextMenu(Rectangle taskShape) {
        Menu setResource = new Menu("Назначить ресурс");
        MenuItem getProperties = new MenuItem("Свойства");
        getProperties.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.setSelectedTask(task);
                UIControl.launchPropertiesTaskWindow(mainWindow);
            }
        });
        MenuItem removeTask = new MenuItem("Удалить задачу");

        ContextMenu contextMenu = new ContextMenu(setResource, getProperties, removeTask);

        taskShape.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isSecondaryButtonDown()) {
                    contextMenu.show(GanttChartObject.this, event.getScreenX(), event.getScreenY());
                }
            }
        });
    }
}
