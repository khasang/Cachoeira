package ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.taskbar;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import ru.khasang.cachoeira.commands.task.SetTaskFinishDateCommand;
import ru.khasang.cachoeira.commands.task.SetTaskStartAndFinishDateCommand;
import ru.khasang.cachoeira.commands.task.SetTaskStartDateCommand;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.vcontroller.contextmenus.TaskContextMenu;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class TaskBar extends Pane {
    private static final double TASK_HEIGHT = 18;   //высота прямоугольника задачи
    protected static final double ROW_HEIGHT = 31;

    protected MainWindowController controller;

    protected ITask task;
    private IResource resource;
    private TaskContextMenu taskContextMenu;
    protected boolean wasMovedByMouse = false;
    protected int rowIndex;                        //координата Y (строка задачи)

    protected Rectangle backgroundRectangle;
    protected Rectangle donePercentRectangle;

    public TaskBar() {
        this.setPadding(new Insets(0, 0, 5, 0));
    }

    public void initTaskRectangle(ITask task,
                                  IResource resource) {
        backgroundRectangle = new Rectangle();
        backgroundRectangle.setFill(Color.valueOf("#03A9F4"));    //цвет прямоугольника
        backgroundRectangle.setStroke(Color.valueOf("#03bdf4"));  //цвет окантовки
        backgroundRectangle.setArcHeight(5);                      //скругление углов
        backgroundRectangle.setArcWidth(5);
        backgroundRectangle.setHeight(TASK_HEIGHT);

        this.setParameters(task, resource, backgroundRectangle);

        donePercentRectangle = new Rectangle();
        donePercentRectangle.setFill(Color.valueOf("#0381f4"));
        donePercentRectangle.arcHeightProperty().bind(backgroundRectangle.arcHeightProperty());
        donePercentRectangle.arcWidthProperty().bind(backgroundRectangle.arcWidthProperty());
        donePercentRectangle.layoutYProperty().bind(backgroundRectangle.layoutYProperty().add(1.25));
        donePercentRectangle.heightProperty().bind(backgroundRectangle.heightProperty().subtract(2.5));
        //ширина зависит от ширины backgroundRectangle и task.donePercent
        donePercentRectangle.widthProperty().bind(
                backgroundRectangle.widthProperty().divide(100).multiply(task.donePercentProperty()));
        //также привязываем все ивенты от backgroundRectangle
        donePercentRectangle.onMousePressedProperty().bind(backgroundRectangle.onMousePressedProperty());
        donePercentRectangle.onMouseDraggedProperty().bind(backgroundRectangle.onMouseDraggedProperty());

        this.getChildren().add(backgroundRectangle);
        this.getChildren().add(donePercentRectangle);

        this.enableDrag(task, backgroundRectangle);
        this.enableResize(task, backgroundRectangle);

        this.setListeners(task, resource, backgroundRectangle, donePercentRectangle);
    }

    protected void setLabel(ITask task, Rectangle backgroundRectangle) {
        // Вешаем лэйбл с наименованием задачи
        Label taskLabel = new Label(task.getName());
        taskLabel.setTextOverrun(OverrunStyle.CLIP);
        taskLabel.setTextFill(Color.WHITE);
        taskLabel.setLayoutY(6);
        taskLabel.prefWidthProperty().bind(backgroundRectangle.widthProperty());
        taskLabel.onMousePressedProperty().bind(backgroundRectangle.onMousePressedProperty());
        taskLabel.onMouseDraggedProperty().bind(backgroundRectangle.onMouseDraggedProperty());
        this.getChildren().add(taskLabel);
    }

    protected abstract void setParameters(ITask task,
                                          IResource resource,
                                          Rectangle backgroundRectangle);

    protected double taskWidth(LocalDate taskStartDate,
                               LocalDate taskFinishDate,
                               int columnWidth) {
        return (ChronoUnit.DAYS.between(taskStartDate, taskFinishDate) * columnWidth);
    }

    protected abstract double taskY(int rowIndex);

    protected double taskX(LocalDate taskStartDate,
                           LocalDate projectStartDate,
                           int columnWidth) {
        return ((ChronoUnit.DAYS.between(projectStartDate, taskStartDate)) * columnWidth) - 1.5;
    }

    /**
     * Метод для анимации движения объекта
     *
     * @param target   Параметр который двигаем
     * @param endValue Параметр который задает конечную точку движения
     * @param duration Время движения в миллисекундах
     * @return Возвращает Timeline
     */
    protected Timeline createTimelineAnimation(DoubleProperty target, double endValue, double duration) {
        KeyValue endKeyValue = new KeyValue(target, endValue, Interpolator.SPLINE(0.4, 0, 0.2, 1));
        KeyFrame endKeyFrame = new KeyFrame(Duration.millis(duration), endKeyValue);
        return new Timeline(endKeyFrame);
    }

    protected abstract void setListeners(ITask task,
                                         IResource resource,
                                         Rectangle backgroundRectangle,
                                         Rectangle donePercentRectangle);

    public void setContextMenu(ITask task) {
        taskContextMenu = new TaskContextMenu(controller.getProject(), task, controller);
        taskContextMenu.initMenus();
    }

    public void setTooltip(Tooltip tooltip) {
        Tooltip.install(this, tooltip);
    }

    /**
     * Метод который включает возможность перемещения метки с помощью мышки по оси Х
     *
     * @param task                Задача этой метки
     * @param backgroundRectangle Прямоугольник
     */
    private void enableDrag(ITask task,
                            Rectangle backgroundRectangle) {
        final Delta dragDelta = new Delta();
        final OldRound oldRound = new OldRound();
        backgroundRectangle.setOnMousePressed(event -> {
            // Выделяем нужный элемент в таблице
            int i = controller.getProject().getTaskList().indexOf(task);
            controller.getTaskTableView().getSelectionModel().select(i);
            if (event.isPrimaryButtonDown()) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = this.getLayoutX() - event.getSceneX();
                getScene().setCursor(Cursor.MOVE);
            }
            // Условие для контекстного меню
            if (event.isSecondaryButtonDown()) {
                taskContextMenu.show(TaskBar.this, event.getScreenX(), event.getScreenY());
            }
        });
        backgroundRectangle.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                double newX = event.getSceneX() + dragDelta.x;
                if (newX > 0 && newX + backgroundRectangle.getWidth() <= this.getParent().getBoundsInParent().getWidth()) {
                    // Хреначим привязку к сетке
                    if (Math.round(newX / controller.getZoomMultiplier()) != oldRound.old) {
                        oldRound.old = Math.round(newX / controller.getZoomMultiplier());
                        this.setLayoutX(Math.round(newX / controller.getZoomMultiplier()) * controller.getZoomMultiplier() - 1.5);
                        wasMovedByMouse = true; // Когда начитаем двигать, то тру, чтобы не началась рекурсия
                        controller.getCommandExecutor().execute(new SetTaskStartAndFinishDateCommand(
                                task,
                                controller.getProject().getStartDate().plusDays(
                                        Math.round(newX / controller.getZoomMultiplier())),
                                Math.round(backgroundRectangle.getWidth() / controller.getZoomMultiplier())
                        ));
                        wasMovedByMouse = false; // Когда окончили движение фолз
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

    /**
     * Метод который включает возможность изменения размера метки с помощью мышки.
     * <p>
     * Для этого создаем еще два мелких прямоугольника, привязываем их к правой и левой сторонам
     * того прямоугольника размер которого хотим менять. Далее прописываем драг ивенты, как в enableDrag()
     * к обоим прямоугольникам. Таким образом получается, что при изменении местоположения левого прямоугольника
     * меняется начальная координата главного прямоугольника (this.setLayout(x))
     * и увеличивается ширина на пройденное расстояние (backgroundRectangle.setWidth(x - delta)). А с помощью
     * правого прямоугольника увеличивается, либо уменьшается ширина главного
     * прямоугольника (backgroundRectangle.setWidth(x)).
     * <p>
     * Проще простого.
     *
     * @param task                Задача этой метки
     * @param backgroundRectangle Прямоугольник
     */
    public void enableResize(ITask task,
                             Rectangle backgroundRectangle) {
        // Создаем прозрачный прямоугольник шириной 10 пикселей
        Rectangle leftResizeHandle = new Rectangle();
        this.getChildren().add(leftResizeHandle);
        leftResizeHandle.setFill(Color.TRANSPARENT);
//        leftResizeHandle.setWidth(4);
        leftResizeHandle.widthProperty().bind(backgroundRectangle.widthProperty().divide(100).multiply(5));
        // Привязываем этот прямоугольник к левой стороне таскбара
        leftResizeHandle.xProperty().bind(backgroundRectangle.xProperty());
        leftResizeHandle.heightProperty().bind(backgroundRectangle.heightProperty());
        leftResizeHandle.layoutYProperty().bind(backgroundRectangle.layoutYProperty());
        // При наведении на левую сторону таскбара будет меняться курсор
        leftResizeHandle.hoverProperty().addListener(observable -> {
            if (leftResizeHandle.isHover()) {
                getScene().setCursor(Cursor.H_RESIZE);
                leftResizeHandle.setFill(Color.valueOf("#0381f4"));
            } else {
                getScene().setCursor(Cursor.DEFAULT);
                leftResizeHandle.setFill(Color.TRANSPARENT);
            }
        });

        final Delta dragDeltaLeft = new Delta();
        final OldRound oldRoundLeft = new OldRound();
        // Ивент при нажатии на прямоугольник
        leftResizeHandle.setOnMousePressed(event -> {
            // Выделяем нужный элемент в таблице
            int i = controller.getProject().getTaskList().indexOf(task);
            controller.getTaskTableView().getSelectionModel().select(i);
            if (event.isPrimaryButtonDown()) {
                // record a delta distance for the drag and drop operation.
                dragDeltaLeft.x = getLayoutX() - event.getSceneX();
                getScene().setCursor(Cursor.H_RESIZE);
            }
        });
        // Ивент при движении прямоугольника
        leftResizeHandle.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                double newX = event.getSceneX() + dragDeltaLeft.x;
                if (newX >= 0 && newX <= getLayoutX() + backgroundRectangle.getWidth()) {
                    // Хреначим привязку к сетке
                    if (Math.round(newX / controller.getZoomMultiplier()) != oldRoundLeft.old) {
                        if (!(Math.round(newX / controller.getZoomMultiplier()) * controller.getZoomMultiplier() - 1.5 == getLayoutX() + backgroundRectangle.getWidth())) { // Условие против нулевой длины тасбара
                            oldRoundLeft.old = Math.round(newX / controller.getZoomMultiplier());
                            double oldX = getLayoutX();
                            this.setLayoutX(Math.round(newX / controller.getZoomMultiplier()) * controller.getZoomMultiplier() - 1.5);
                            backgroundRectangle.setWidth(backgroundRectangle.getWidth() - (this.getLayoutX() - oldX));
                            wasMovedByMouse = true; // Когда начитаем двигать, то тру, чтобы не началась рекурсия
                            controller.getCommandExecutor().execute(new SetTaskStartDateCommand(
                                    task,
                                    controller.getProject().getStartDate().plusDays((Math.round(newX / controller.getZoomMultiplier())))));
                            wasMovedByMouse = false; // Когда окончили движение фолз
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

        // Создаем прозрачный прямоугольник шириной 10 пикселей
        Rectangle rightResizeHandle = new Rectangle();
        this.getChildren().add(rightResizeHandle);
        rightResizeHandle.setFill(Color.TRANSPARENT);
//        rightResizeHandle.setWidth(4);
        rightResizeHandle.widthProperty().bind(backgroundRectangle.widthProperty().divide(100).multiply(5));
        // Привязываем этот прямоугольник к правой стороне таскбара
        rightResizeHandle.xProperty().bind(
                backgroundRectangle.xProperty()
                        .add(backgroundRectangle.widthProperty())
                        .subtract(rightResizeHandle.widthProperty())
        );
        rightResizeHandle.heightProperty().bind(backgroundRectangle.heightProperty());
        rightResizeHandle.layoutYProperty().bind(backgroundRectangle.layoutYProperty());
        // При наведении на левую сторону таскбара будет меняться курсор
        rightResizeHandle.hoverProperty().addListener(observable -> {
            if (rightResizeHandle.isHover()) {
                getScene().setCursor(Cursor.H_RESIZE);
                rightResizeHandle.setFill(Color.valueOf("#0381f4"));
            } else {
                getScene().setCursor(Cursor.DEFAULT);
                rightResizeHandle.setFill(Color.TRANSPARENT);
            }
        });

        final Delta dragDeltaRight = new Delta();
        final OldRound oldRoundRight = new OldRound();
        // Ивент при нажатии на прямоугольник
        rightResizeHandle.setOnMousePressed(event -> {
            // Выделяем нужный элемент в таблице
            controller.getTaskTableView().getSelectionModel().select(rowIndex);
            if (event.isPrimaryButtonDown()) {
                // record a delta distance for the drag and drop operation.
                dragDeltaRight.x = backgroundRectangle.getWidth() - event.getSceneX();
                getScene().setCursor(Cursor.H_RESIZE);
            }
        });
        // Ивент при движении прямоугольника
        rightResizeHandle.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                double newWidth = event.getSceneX() + dragDeltaRight.x;
                if (newWidth >= controller.getZoomMultiplier() && this.getLayoutX() + newWidth <= this.getParent().getBoundsInLocal().getWidth()) {
                    // Хреначим привязку к сетке
                    if (Math.round(newWidth / controller.getZoomMultiplier()) != oldRoundRight.old) {
                        oldRoundRight.old = Math.round(newWidth / controller.getZoomMultiplier());
                        backgroundRectangle.setWidth(Math.round(newWidth / controller.getZoomMultiplier()) * controller.getZoomMultiplier());
                        wasMovedByMouse = true; // Когда начитаем двигать, то тру, чтобы не началась рекурсия
                        controller.getCommandExecutor().execute(new SetTaskFinishDateCommand(
                                task,
                                task.getStartDate().plusDays(Math.round(backgroundRectangle.getWidth() / controller.getZoomMultiplier()))));
                        wasMovedByMouse = false; // Когда окончили движение фолз
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

    public ITask getTask() {
        return task;
    }

    public void setTask(ITask task) {
        this.task = task;
    }

    public IResource getResource() {
        return resource;
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
