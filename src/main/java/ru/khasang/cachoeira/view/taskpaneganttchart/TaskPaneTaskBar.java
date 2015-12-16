package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.tooltips.TaskTooltip;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created by truesik on 04.11.2015.
 */
public class TaskPaneTaskBar extends Pane {
    private static final double TASK_HEIGHT = 18;   //высота прямоугольника задачи
    private static final double rowHeight = 31;

    private ITask task;
    private int rowIndex;                        //координата Y (строка задачи)
    private int columnWidth;
    private boolean wasMoved;
    private ContextMenu contextMenu;
    private Rectangle backgroundRectangle;

    public TaskPaneTaskBar(int columnWidth) {
        this.columnWidth = columnWidth;
//        setAlignment(Pos.CENTER_LEFT);

    }

    public void createTaskRectangle(UIControl uiControl,
                                    ITask task) {
        backgroundRectangle = new Rectangle();
        backgroundRectangle.setFill(Color.valueOf("#03A9F4"));    //цвет прямоугольника
        backgroundRectangle.setStroke(Color.valueOf("#B3E5FC"));  //цвет окантовки
        backgroundRectangle.setArcHeight(5);                      //сгругление углов
        backgroundRectangle.setArcWidth(5);
        backgroundRectangle.setHeight(TASK_HEIGHT);

        setParameters(uiControl, task, backgroundRectangle);

        Rectangle donePercentRectangle = new Rectangle();
        donePercentRectangle.setFill(Color.valueOf("#0381f4"));
        donePercentRectangle.arcHeightProperty().bind(backgroundRectangle.arcHeightProperty());
        donePercentRectangle.arcWidthProperty().bind(backgroundRectangle.arcWidthProperty());
        donePercentRectangle.yProperty().bind(backgroundRectangle.yProperty().add(1.25));
        donePercentRectangle.heightProperty().bind(backgroundRectangle.heightProperty().subtract(2.5));
        donePercentRectangle.widthProperty().bind(
                backgroundRectangle.widthProperty().divide(100).multiply(task.donePercentProperty()));
        donePercentRectangle.onMousePressedProperty().bind(backgroundRectangle.onMousePressedProperty());
        donePercentRectangle.onMouseDraggedProperty().bind(backgroundRectangle.onMouseDraggedProperty());

        this.getChildren().add(backgroundRectangle);
        this.getChildren().add(donePercentRectangle);
        enableResize(uiControl);

        setListeners(uiControl, task, backgroundRectangle, donePercentRectangle);
    }

    private void setParameters(UIControl uiControl,
                               ITask task,
                               Rectangle backgroundRectangle) {
        backgroundRectangle.setWidth(taskWidth(task.getStartDate(), task.getFinishDate()));
        this.setLayoutX(taskX(task.getStartDate(), uiControl.getController().getProject().getStartDate()));
        this.setLayoutY(taskY(uiControl.getController().getProject().getTaskList().indexOf(task)));
    }

    private double taskWidth(LocalDate taskStartDate,
                             LocalDate taskFinishDate) {
        return (ChronoUnit.DAYS.between(taskStartDate, taskFinishDate) * columnWidth);
    }

    private double taskY(int rowIndex) {
        this.rowIndex = rowIndex;
        return (rowIndex * rowHeight) + 6.5;
    }

    private double taskX(LocalDate taskStartDate,
                         LocalDate projectStartDate) {
        return ((ChronoUnit.DAYS.between(projectStartDate, taskStartDate)) * columnWidth) - 1.5;
    }

    private void setListeners(UIControl uiControl,
                              ITask task,
                              Rectangle backgroundRectangle,
                              Rectangle donePercentRectangle) {
        /**
         * Следим за изменениями в списке задач, если произошло добавление или удаление элемента в списке,
         * то пересчитываем индексы у элементов на диаграмме
         */
        uiControl.getController().getProject().getTaskList().addListener((ListChangeListener<ITask>) c -> {
            while (c.next()) {
                if (c.wasRemoved() || c.wasAdded()) {
                    KeyValue endKeyValue = new KeyValue(this.layoutYProperty(), taskY(uiControl.getController().getProject().getTaskList().indexOf(task)), Interpolator.SPLINE(0.4, 0, 0.2, 1));
                    KeyFrame endKeyFrame = new KeyFrame(Duration.millis((rowIndex + 1) * 150), endKeyValue);
                    Timeline timeline = new Timeline(endKeyFrame);
                    timeline.play();
//                    this.setLayoutY(taskY(uiControl.getController().getProject().getTaskList().indexOf(task)));
                }
            }
        });
        /** Следим за изменением начальной и конечной даты */
        task.startDateProperty().addListener((observable, oldValue, newValue) -> {
            if (!wasMoved) {
                backgroundRectangle.setWidth(taskWidth(task.getStartDate(), task.getFinishDate()));
                this.setLayoutX(taskX(task.getStartDate(), uiControl.getController().getProject().getStartDate()));
            }
        });
        task.finishDateProperty().addListener((observable, oldValue, newValue) -> {
            if (!wasMoved) {
                backgroundRectangle.setWidth(taskWidth(task.getStartDate(), task.getFinishDate()));
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
                backgroundRectangle.setFill(Color.valueOf("03bdf4"));
                backgroundRectangle.setStroke(Color.valueOf("#03d1f4"));
                donePercentRectangle.setFill(Color.valueOf("#0395f4"));
            } else {
                backgroundRectangle.setFill(Color.valueOf("#03A9F4"));    //цвет прямоугольника
                backgroundRectangle.setStroke(Color.valueOf("#03bdf4"));
                donePercentRectangle.setFill(Color.valueOf("#0381f4"));
            }
        });
    }

    public void setContextMenu(UIControl uiControl,
                               ITask task) {
        Menu setResource = new Menu("Назначить ресурс");
        MenuItem getProperties = new MenuItem("Свойства");
        getProperties.setOnAction(event -> uiControl.getController().selectedTaskProperty().setValue(task));
        MenuItem removeTask = new MenuItem("Удалить задачу");

        contextMenu = new ContextMenu(setResource, getProperties, removeTask);
    }

    public void setTooltip(TaskTooltip taskTooltip) {
        Tooltip.install(this, taskTooltip);
    }

    /**
     * Drag'n'Drop
     */
    public void enableDrag(UIControl uiControl,
                           ITask task) {
        final Delta dragDelta = new Delta();
        final OldRound oldRound = new OldRound();
        backgroundRectangle.setOnMousePressed(event -> {
            /** Выделяем нужный элемент в таблице */
            uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController().getTaskTreeTableView().getSelectionModel().select(rowIndex);
            if (event.isPrimaryButtonDown()) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = getLayoutX() - event.getSceneX();
                getScene().setCursor(Cursor.MOVE);
            } else {
                getScene().setCursor(Cursor.DEFAULT);
            }
            /** Условие для контекстного меню */
            if (event.isSecondaryButtonDown()) {
                contextMenu.show(TaskPaneTaskBar.this, event.getScreenX(), event.getScreenY());
            }
        });
        backgroundRectangle.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                double newX = event.getSceneX() + dragDelta.x;
                if (newX > 0 && newX < getScene().getWidth()) {
                    /** Хреначим привязку к сетке */
                    double v = newX / columnWidth; // Делим координату на ширину столбца, получаем цифру в днях с десятыми
                    long newRound = Math.round(v); // Убираем десятые, чтобы был ровно день
                    if (newRound != oldRound.old) {
                        oldRound.old = newRound;
                        long l = newRound * columnWidth; // Конвертим обратно в пиксели
                        setLayoutX(l - 2);
                        wasMoved = true; // Когда начитаем двигать, то тру, чтобы не началась рекурсия
                        task.setStartDate(uiControl.getController().getProject().getStartDate().plusDays(newRound));
                        task.setFinishDate(task.getStartDate().plusDays(Math.round(this.getWidth() / columnWidth)));
                        wasMoved = false; // Когда окончили движение фолз
                    }
                }
            }
        });
        backgroundRectangle.setOnMouseExited(event -> {
            if (!event.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });
    }

    public void enableResize(UIControl uiControl) {

        Rectangle resizeHandleLeft = new Rectangle();
        this.getChildren().add(resizeHandleLeft);
        resizeHandleLeft.setFill(Color.GOLD);
        resizeHandleLeft.setWidth(10);
        // bind to top left corner of Rectangle:
        resizeHandleLeft.xProperty().bind(backgroundRectangle.xProperty());
        resizeHandleLeft.heightProperty().bind(backgroundRectangle.heightProperty());
        resizeHandleLeft.yProperty().bind(backgroundRectangle.yProperty());
        resizeHandleLeft.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (resizeHandleLeft.isHover()) {
                getScene().setCursor(Cursor.H_RESIZE);
            } else {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });


        Rectangle resizeHandleRight = new Rectangle();
        this.getChildren().add(resizeHandleRight);
        resizeHandleRight.setFill(Color.TRANSPARENT);
        resizeHandleRight.setWidth(10);
        // bind to bottom right corner of Rectangle:
        resizeHandleRight.xProperty().bind(backgroundRectangle.widthProperty().subtract(10));
        resizeHandleRight.heightProperty().bind(backgroundRectangle.heightProperty());
        resizeHandleRight.yProperty().bind(backgroundRectangle.yProperty());
        resizeHandleRight.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (resizeHandleRight.isHover()) {
                getScene().setCursor(Cursor.H_RESIZE);
            } else {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });

//        resizeHandleLeft.setOnMouseDragged(event -> {
//            if (mouseLocation.value != null) {
//                double deltaX = event.getSceneX() - mouseLocation.value.getX();
//                double newX = backgroundRectangle.getX() + deltaX;
//                if (newX >= 10
//                        && newX <= backgroundRectangle.getX() + backgroundRectangle.getWidth() - 10) {
//                    backgroundRectangle.setX(newX);
//                    backgroundRectangle.setWidth(backgroundRectangle.getWidth() - deltaX);
//                }
//                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
//            }
//        });

        final Delta dragDelta = new Delta();
        final OldRound oldRound = new OldRound();
        resizeHandleRight.setOnMousePressed(event -> {
            /** Выделяем нужный элемент в таблице */
            uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController().getTaskTreeTableView().getSelectionModel().select(rowIndex);
            if (event.isPrimaryButtonDown()) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = backgroundRectangle.getWidth() - event.getSceneX();
                getScene().setCursor(Cursor.H_RESIZE);
            } else {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });
        resizeHandleRight.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.H_RESIZE);
                double newX = event.getSceneX() + dragDelta.x;
                if (newX >= columnWidth && newX < getScene().getWidth()) {
                    /** Хреначим привязку к сетке */
                    double v = newX / columnWidth; // Делим координату на ширину столбца, получаем цифру в днях с десятыми
                    long newRound = Math.round(v); // Убираем десятые, чтобы был ровно день
                    if (newRound != oldRound.old) {
                        oldRound.old = newRound;
                        long l = newRound * columnWidth; // Конвертим обратно в пиксели
                        backgroundRectangle.setWidth(l);
                        wasMoved = true; // Когда начитаем двигать, то тру, чтобы не началась рекурсия
                        System.out.println(backgroundRectangle.getWidth() / columnWidth);
                        task.setFinishDate(task.getStartDate().plusDays(Math.round(backgroundRectangle.getWidth() / columnWidth)));
                        wasMoved = false; // Когда окончили движение фолз
                    }
                }
            } else {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });
    }

    public ITask getTask() {
        return task;
    }

    public void setTask(ITask task) {
        this.task = task;
    }

    // records relative x co-ordinate.
    private class Delta {
        double x;
    }

    private class OldRound {
        long old;
    }

    private class Wrapper<T> {
        T value;
    }
}
