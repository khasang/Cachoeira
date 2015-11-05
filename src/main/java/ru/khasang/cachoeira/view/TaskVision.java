package ru.khasang.cachoeira.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
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
public class TaskVision extends HBox {
    private static final double TASK_HEIGHT = 18;   //высота прямоугольника задачи
    private static final double SPACING = 10;       //расстояние между элементами (прямоугольником задачи и лэйблами с названиями ресурсов)
    private static final double MULTIPLIER = 30;    //множитель (длинна одного дня в пикселях) todo когда нужен будет зум перевести в переменную
    private IController controller;
    private UIControl UIControl;
    private MainWindow mainWindow;
    private ITask task;
    private double rowIndex;                        //координата Y (строка задачи)

    public TaskVision(IController controller, UIControl UIControl, MainWindow mainWindow, ITask task, double rowIndex) {
        this.controller = controller;
        this.UIControl = UIControl;
        this.mainWindow = mainWindow;
        this.task = task;
        this.rowIndex = rowIndex;
        setSpacing(SPACING);


        double startDay = ((task.getStartDate().getTime() - 1446498000000L) / (24 * 60 * 60 * 1000)) * MULTIPLIER; //координата Х (дата начала задачи минус дата начала проекта (1446498000000L = 03.11.15 в миллисекундах от ~1977 года, нужно будет заменить на controller.getProject().getStartDate().getTime()) получаем разницу в днях и умножаем ее на длину дня в пикселях(MULTIPLIER)
        setLayoutX(startDay);
        setLayoutY(rowIndex);

        double taskWidth = ((task.getFinishDate().getTime() - task.getStartDate().getTime()) / (24 * 60 * 60 * 1000)) * MULTIPLIER; // длина прямоугольника (разница между

        Rectangle taskShape = new Rectangle(0, 0, taskWidth, TASK_HEIGHT); //создаем прямоугольник
        taskShape.setFill(Color.valueOf("#03A9F4"));    //цвет прямоугольника
        taskShape.setStroke(Color.valueOf("#B3E5FC"));  //цвет окантовки
        taskShape.setArcHeight(5);                      //сгругление углов
        taskShape.setArcWidth(5);                       //сгругление углов

        getChildren().add(taskShape); //добавляем прямоугольник на HBox (панель)

        for (IResource resource : task.getResourceList()) {
            Label resourceName = new Label(resource.getName());
            getChildren().add(resourceName); //добавляем ресурсы на HBox
        }
        setContextMenu(taskShape); //контекстное меню при нажатии на прямоугольник
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
//        TaskVision taskVision = this;

        taskShape.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isSecondaryButtonDown()) {
                    contextMenu.show(TaskVision.this, event.getScreenX(), event.getScreenY());
                }
            }
        });
    }
}
