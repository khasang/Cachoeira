package ru.khasang.cachoeira.view.resourcepaneganttchart;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.contextmenus.TaskContextMenu;
import ru.khasang.cachoeira.view.tooltips.TaskTooltip;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created by truesik on 04.11.2015.
 */
public class ResourcePaneTaskBar extends Pane {
    private static final double TASK_HEIGHT = 18;   //высота прямоугольника задачи
    private static final double rowHeight = 31;

    private ITask task;
    private IResource resource;
    private int columnWidth;
    private boolean wasMoved;
    private TaskContextMenu taskContextMenu;

    public ResourcePaneTaskBar(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public void createTaskRectangle(UIControl uiControl,
                                    ITask task,
                                    IResource resource) {
        Rectangle backgroundRectangle = new Rectangle();
        backgroundRectangle.setFill(Color.valueOf("#03A9F4"));    //цвет прямоугольника
        backgroundRectangle.setStroke(Color.valueOf("#B3E5FC"));  //цвет окантовки
        backgroundRectangle.setArcHeight(5);                      //сгругление углов
        backgroundRectangle.setArcWidth(5);
        backgroundRectangle.setHeight(TASK_HEIGHT);

        setParameters(uiControl, task, resource, backgroundRectangle);

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

        enableDrag(uiControl, task, backgroundRectangle);
        enableResize(uiControl, task, backgroundRectangle);

        setListeners(uiControl, task, resource, backgroundRectangle, donePercentRectangle);
    }

    private void setParameters(UIControl uiControl,
                               ITask task,
                               IResource resource,
                               Rectangle backgroundRectangle) {
        backgroundRectangle.setWidth(taskWidth(task.getStartDate(), task.getFinishDate()));
        this.setLayoutX(taskX(task.getStartDate(), uiControl.getController().getProject().getStartDate()));
        this.setLayoutY(taskY(uiControl.getController().getProject().getResourceList().indexOf(resource)));
    }

    private double taskWidth(LocalDate taskStartDate,
                             LocalDate taskFinishDate) {
        return (ChronoUnit.DAYS.between(taskStartDate, taskFinishDate) * columnWidth);
    }

    private double taskY(int resourceRowIndex) {
        return (resourceRowIndex * rowHeight) + 6.5;
    }

    private double taskX(LocalDate taskStartDate,
                         LocalDate projectStartDate) {
        return ((ChronoUnit.DAYS.between(projectStartDate, taskStartDate)) * columnWidth) - 1.5;
    }

    private void setListeners(UIControl uiControl,
                              ITask task,
                              IResource resource,
                              Rectangle backgroundRectangle,
                              Rectangle donePercentRectangle) {
        /**
         * Следим за изменениями в списке ресурсов, если произошло добавление или удаление элемента в списке,
         * то пересчитываем индексы у элементов на диаграмме
         */
        uiControl.getController().getProject().getResourceList().addListener((ListChangeListener<IResource>) change -> {
            while (change.next()) {
                if (change.wasRemoved() || change.wasAdded()) {
                    /** Анимация при удалиении элемента из таблицы */
                    KeyValue endKeyValue = new KeyValue(this.layoutYProperty(), taskY(uiControl.getController().getProject().getResourceList().indexOf(resource)), Interpolator.SPLINE(0.4, 0, 0.2, 1));
                    KeyFrame endKeyFrame = new KeyFrame(Duration.millis((uiControl.getController().getProject().getResourceList().indexOf(resource) + 1) * 150), endKeyValue);
                    Timeline timeline = new Timeline(endKeyFrame);
                    timeline.play();
//                    this.setLayoutY(taskY(uiControl.getController().getProject().getResourceList().indexOf(resource)));
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

    public void setContextMenu(IController controller,
                               ITask task) {
        taskContextMenu = new TaskContextMenu();
        taskContextMenu.initMenus(controller, task);
    }

    public void setTooltip(TaskTooltip taskTooltip) {
        Tooltip.install(this, taskTooltip);
    }

    public ITask getTask() {
        return task;
    }

    public IResource getResource() {
        return resource;
    }

    /**
     * Drag'n'Drop
     */
    public void enableDrag(UIControl uiControl,
                           ITask task,
                           Rectangle backgroundRectangle) {
        final Delta dragDelta = new Delta();
        final OldRound oldRound = new OldRound();
        backgroundRectangle.setOnMousePressed(event -> {
            /** Выделяем нужный элемент в таблице */
            int i = uiControl.getController().getProject().getTaskList().indexOf(task);
            uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController().getTaskTreeTableView().getSelectionModel().select(i);
            if (event.isPrimaryButtonDown()) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = getLayoutX() - event.getSceneX();
                getScene().setCursor(Cursor.MOVE);
            } else {
                getScene().setCursor(Cursor.DEFAULT);
            }
            /** Условие для контекстного меню */
            if (event.isSecondaryButtonDown()) {
                taskContextMenu.show(ResourcePaneTaskBar.this, event.getScreenX(), event.getScreenY());
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
                        wasMoved = true; // Когда начитаем двигать, то тру, чтобы не началась рекурсия
                        task.setStartDate(uiControl.getController().getProject().getStartDate().plusDays(newRound));
                        task.setFinishDate(task.getStartDate().plusDays(Math.round(getWidth() / columnWidth)));
                        long l = newRound * columnWidth; // Конвертим обратно в пиксели
                        setLayoutX(l - 2);
                        wasMoved = false; // Когда окончили движение фолз
                    }
                }
            }
        });
        backgroundRectangle.setOnMouseReleased(event -> {
            if (!event.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });
    }

    public void enableResize(UIControl uiControl,
                             ITask task,
                             Rectangle backgroundRectangle) {
        /** Создаем прозрачный прямоугольник шириной 10 пикселей */
        Rectangle leftResizeHandle = new Rectangle();
        this.getChildren().add(leftResizeHandle);
        leftResizeHandle.setFill(Color.TRANSPARENT);
        leftResizeHandle.setWidth(10);
        /** Привязываем этот прямоугольник к левой стороне таскбара */
        leftResizeHandle.xProperty().bind(backgroundRectangle.xProperty());
        leftResizeHandle.heightProperty().bind(backgroundRectangle.heightProperty());
        leftResizeHandle.yProperty().bind(backgroundRectangle.yProperty());
        /** При наведении на левую сторону таскбара будет меняться курсор */
        leftResizeHandle.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (leftResizeHandle.isHover()) {
                getScene().setCursor(Cursor.H_RESIZE);
            } else {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });

        final Delta dragDeltaLeft = new Delta();
        final OldRound oldRoundLeft = new OldRound();
        /** Ивент при нажатии на прямоугольник */
        leftResizeHandle.setOnMousePressed(event -> {
            /** Выделяем нужный элемент в таблице */
            int i = uiControl.getController().getProject().getTaskList().indexOf(task);
            uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController().getTaskTreeTableView().getSelectionModel().select(i);
            if (event.isPrimaryButtonDown()) {
                // record a delta distance for the drag and drop operation.
                dragDeltaLeft.x = getLayoutX() - event.getSceneX();
                getScene().setCursor(Cursor.H_RESIZE);
            }
        });
        /** Ивент при движении прямоугольника */
        leftResizeHandle.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                double newX = event.getSceneX() + dragDeltaLeft.x;
                if (newX >= 0 && newX <= getLayoutX() + backgroundRectangle.getWidth()) {
                    /** Хреначим привязку к сетке */
                    if (Math.round(newX / columnWidth) != oldRoundLeft.old) {
                        if (!(Math.round(newX / columnWidth) * columnWidth - 2 == getLayoutX() + backgroundRectangle.getWidth())) { // Условие против нулевой длины тасбара
                            oldRoundLeft.old = Math.round(newX / columnWidth);
                            double oldX = getLayoutX();
                            setLayoutX(Math.round(newX / columnWidth) * columnWidth - 2);
                            backgroundRectangle.setWidth(backgroundRectangle.getWidth() - (getLayoutX() - oldX));
                            wasMoved = true; // Когда начитаем двигать, то тру, чтобы не началась рекурсия
                            task.setStartDate(uiControl.getController().getProject().getStartDate().plusDays((Math.round(newX / columnWidth))));
                            wasMoved = false; // Когда окончили движение фолз
                        }
                    }
                }
            }
        });
        leftResizeHandle.setOnMouseReleased(event -> {
            if (!event.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });

        /** Создаем прозрачный прямоугольник шириной 10 пикселей */
        Rectangle rightResizeHandle = new Rectangle();
        this.getChildren().add(rightResizeHandle);
        rightResizeHandle.setFill(Color.TRANSPARENT);
        rightResizeHandle.setWidth(10);
        /** Привязываем этот прямоугольник к правой стороне таскбара */
        rightResizeHandle.xProperty().bind(backgroundRectangle.xProperty().add(backgroundRectangle.widthProperty()).subtract(10));
        rightResizeHandle.heightProperty().bind(backgroundRectangle.heightProperty());
        rightResizeHandle.yProperty().bind(backgroundRectangle.yProperty());
        /** При наведении на левую сторону таскбара будет меняться курсор */
        rightResizeHandle.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (rightResizeHandle.isHover()) {
                getScene().setCursor(Cursor.H_RESIZE);
            } else {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });

        final Delta dragDeltaRight = new Delta();
        final OldRound oldRoundRight = new OldRound();
        /** Ивент при нажатии на прямоугольник */
        rightResizeHandle.setOnMousePressed(event -> {
            /** Выделяем нужный элемент в таблице */
            int i = uiControl.getController().getProject().getTaskList().indexOf(task);
            uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController().getTaskTreeTableView().getSelectionModel().select(i);
            if (event.isPrimaryButtonDown()) {
                // record a delta distance for the drag and drop operation.
                dragDeltaRight.x = backgroundRectangle.getWidth() - event.getSceneX();
                getScene().setCursor(Cursor.H_RESIZE);
            }
        });
        /** Ивент при движении прямоугольника */
        rightResizeHandle.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                double newWidth = event.getSceneX() + dragDeltaRight.x;
                if (newWidth >= columnWidth && newWidth < getScene().getWidth()) {
                    /** Хреначим привязку к сетке */
                    if (Math.round(newWidth / columnWidth)!= oldRoundRight.old) {
                        oldRoundRight.old = Math.round(newWidth / columnWidth);
                        backgroundRectangle.setWidth(Math.round(newWidth / columnWidth) * columnWidth);
                        wasMoved = true; // Когда начитаем двигать, то тру, чтобы не началась рекурсия
                        task.setFinishDate(task.getStartDate().plusDays(Math.round(backgroundRectangle.getWidth() / columnWidth)));
                        wasMoved = false; // Когда окончили движение фолз
                    }
                }
            }
        });
        rightResizeHandle.setOnMouseReleased(event -> {
            if (!event.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });
    }

    public void setTask(ITask task) {
        this.task = task;
    }

    public void setResource(IResource resource) {
        this.resource = resource;
    }

    // records relative x co-ordinate.
    private class Delta {
        double x;
    }

    private class OldRound {
        long old;
    }
}
