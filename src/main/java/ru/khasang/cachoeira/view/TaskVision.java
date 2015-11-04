package ru.khasang.cachoeira.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
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
    private static final double TASK_HEIGHT = 18;
    private static final double SPACING = 10;
    private IController controller;
    private UIControl UIControl;
    private MainWindow mainWindow;
    private ITask task;
    private double rowIndex;

    public TaskVision(IController controller, UIControl UIControl, MainWindow mainWindow, ITask task, double rowIndex) {
        this.controller = controller;
        this.UIControl = UIControl;
        this.mainWindow = mainWindow;
        this.task = task;
        this.rowIndex = rowIndex;
        setSpacing(SPACING);

        setContextMenu();

        double startDay = ((task.getStartDate().getTime() - 1446498000000L) / (24 * 60 * 60 * 1000)) * 30;
        setLayoutX(startDay);
        setLayoutY(rowIndex);

        double taskWidth = ((task.getFinishDate().getTime() - task.getStartDate().getTime()) / (24 * 60 * 60 * 1000)) * 30;

        Rectangle taskShape = new Rectangle(0, 0, taskWidth, TASK_HEIGHT);
        taskShape.setFill(Color.valueOf("#03A9F4"));
        taskShape.setStroke(Color.valueOf("#B3E5FC"));
        taskShape.setArcHeight(5);
        taskShape.setArcWidth(5);

        getChildren().add(taskShape);

        for (IResource resource : task.getResourceList()) {
            Label resourceName = new Label(resource.getName());
            getChildren().add(resourceName);
        }
    }

    public void setContextMenu() {
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

        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isSecondaryButtonDown()) {
                    contextMenu.show(TaskVision.this, event.getScreenX(), event.getScreenY());
                }
            }
        });
    }
}
