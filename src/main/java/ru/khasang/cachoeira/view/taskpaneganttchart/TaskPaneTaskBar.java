package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.tooltips.TaskTooltip;

import java.time.temporal.ChronoUnit;

/**
 * Created by truesik on 04.11.2015.
 */
public class TaskPaneTaskBar extends Rectangle {
    private static final double TASK_HEIGHT = 18;   //высота прямоугольника задачи
    private static final double rowHeight = 24;

    private IController controller;
    private ITask task;
    private int rowIndex;                        //координата Y (строка задачи)
    private int columnWidth;
    private UIControl uiControl;
    private boolean wasMoved;
    private ContextMenu contextMenu;

    public TaskPaneTaskBar(IController controller, ITask task, int columnWidth, UIControl uiControl) {
        this.controller = controller;
        this.task = task;
        this.columnWidth = columnWidth;
        this.uiControl = uiControl;

        rowIndex = controller.getProject().getTaskList().indexOf(task);

        this.setFill(Color.valueOf("#03A9F4"));    //цвет прямоугольника
        this.setStroke(Color.valueOf("#B3E5FC"));  //цвет окантовки
        this.setArcHeight(5);                      //сгругление углов
        this.setArcWidth(5);                       //сгругление углов

        calculateStartPoint();
        this.setLayoutY((rowIndex * rowHeight) + 3);
        calculateWidth();
        this.setHeight(TASK_HEIGHT);

        /**
         * Следим за изменениями в списке задач, если произошло добавление или удаление элемента в списке,
         * то пересчитываем индексы у элементов на диаграмме
         */
        controller.getProject().getTaskList().addListener(new ListChangeListener<ITask>() {
            @Override
            public void onChanged(Change<? extends ITask> c) {
                while (c.next()) {
                    if (c.wasRemoved() || c.wasAdded()) {
                        rowIndex = controller.getProject().getTaskList().indexOf(task);
                        setLayoutY((rowIndex * rowHeight) + 3);
                    }
                }
            }
        });
        /** Следим за изменением начальной и конечной даты */
        task.startDateProperty().addListener((observable, oldValue, newValue) -> {
            if (!wasMoved) {
                calculateStartPoint();
                calculateWidth();
            }
        });
        task.finishDateProperty().addListener((observable, oldValue, newValue) -> {
            if (!wasMoved) {
                calculateWidth();
            }
        });

        /** Следим за списком ресурсов привязанных к данной задаче */
        task.getResourceList().addListener((ListChangeListener<IResource>) change -> {
            while (change.next()) {
                // Если добавился
                for (IResource resource : change.getAddedSubList()) {
                    uiControl.getMainWindow().getDiagramPaneController().getResourcePaneController().getResourceGanttChart().getResourcePaneObjectsLayer().addTaskBar(task, resource);
                }
                // Если удалился
                for (IResource resource : change.getRemoved()) {
                    uiControl.getMainWindow().getDiagramPaneController().getResourcePaneController().getResourceGanttChart().getResourcePaneObjectsLayer().removeTaskBarByResource(task, resource);
                }
            }
        });

        //подсветка при наведении
        this.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.setFill(Color.valueOf("#41C2FA"));
                this.setStroke(Color.valueOf("#72D1FA"));
            } else {
                this.setFill(Color.valueOf("#03A9F4"));    //цвет прямоугольника
                this.setStroke(Color.valueOf("#B3E5FC"));
            }
        });

        setContextMenu(); //контекстное меню при нажатии на прямоугольник, добавлено для теста (код нужно дописать)
        setTooltip();
        enableDrag();
    }

    private void calculateStartPoint() {
        double startDay = (ChronoUnit.DAYS.between(controller.getProject().getStartDate(), task.getStartDate())) * columnWidth;
        this.setX(startDay - 2); //"2" - подгонка под сетку
    }

    private void calculateWidth() {
        double taskWidth = (ChronoUnit.DAYS.between(task.getStartDate(), task.getFinishDate()) * columnWidth);
        this.setWidth(taskWidth);
    }

    public void setContextMenu() {
        Menu setResource = new Menu("Назначить ресурс");
        MenuItem getProperties = new MenuItem("Свойства");
        getProperties.setOnAction(event -> controller.selectedTaskProperty().setValue(this.task));
        MenuItem removeTask = new MenuItem("Удалить задачу");

        contextMenu = new ContextMenu(setResource, getProperties, removeTask);
    }

    private void setTooltip() {
        TaskTooltip taskTooltip = new TaskTooltip();
        taskTooltip.initToolTip(task);
        Tooltip.install(this, taskTooltip);
    }

    public ITask getTask() {
        return task;
    }

    /**
     * Drag'n'Drop
     */
    private void enableDrag() {
        final Delta dragDelta = new Delta();
        setOnMousePressed(event -> {
            /** Выделяем нужный элемент в таблице */
            uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController().getTaskTreeTableView().getSelectionModel().select(rowIndex);
            if (event.isPrimaryButtonDown()) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = getX() - event.getX();
                getScene().setCursor(Cursor.MOVE);
            } else {
                getScene().setCursor(Cursor.DEFAULT);
            }
            /** Условие для контекстного меню */
            if (event.isSecondaryButtonDown()) {
                contextMenu.show(TaskPaneTaskBar.this, event.getScreenX(), event.getScreenY());
            }
        });
        setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                double newX = event.getX() + dragDelta.x;
                if (newX > 0 && newX < getScene().getWidth()) {
                    /** Хреначим привязку к сетке */
                    double v = newX / columnWidth; // Делим координату на ширину столбца, получаем цифру в днях с десятыми
                    long round = Math.round(v); // Убираем десятые, чтобы был ровно день
                    wasMoved = true; // Когда начитаем двигать, то тру, чтобы не началась рекурсия
                    task.setStartDate(controller.getProject().getStartDate().plusDays(round));
                    task.setFinishDate(task.getStartDate().plusDays(Math.round(getWidth() / columnWidth)));
                    long l = round * columnWidth; // Конвертим обратно в пиксели
                    setX(l - 2);
                    wasMoved = false; // Когда окончили движение фолз
                }
            }
        });
        setOnMouseExited(event -> {
            if (!event.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });
    }

    // records relative x co-ordinate.
    private class Delta {
        double x;
    }
}
