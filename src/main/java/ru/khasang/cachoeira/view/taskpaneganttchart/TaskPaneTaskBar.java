package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Cursor;
import javafx.scene.control.*;
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
 * Класс в котором описывается поведение метки задачи на диаграмме Ганта на вкладке Задачи.
 * <p>
 * Важно понимать, что сам таскбар это панель (Pane), а уже на эту панель накладываются прямоугольники нужной длины.
 * Сделано так потому что нужна была возможность как то менять цвет прямоугольника в зависимости от процента
 * выполнения задачи. Ничего другого, как наложить сверху еще один прямоугольник, я не нашел.
 * Соответвенно главный прямоугольник это backgroundRectangle, а ширина donePercentRectangle меняется в зависимости от
 * task.getDonePercent().
 * <p>
 * Также стоит уяснить, что изменение положения метки на оси Х происходит с помощью this.setLayoutX()
 * или this.layoutXProperty(). А изменение ширины с помощью backgroundRectangle.setWidth или
 * backgroundRectangle.widthProperty(). Такие сложности связаны с тем что TaskBar меняет свою ширину в зависимости от
 * ширины Node'ы, которая в нем (taskBar'е) лежит, т.е. от backgroundRectangle.
 */
public class TaskPaneTaskBar extends Pane {
    private static final double TASK_HEIGHT = 18;   //высота прямоугольника задачи
    private static final double rowHeight = 31;

    private ITask task;
    private int rowIndex;                        //координата Y (строка задачи)
    private boolean wasMoved;
    private TaskContextMenu taskContextMenu;

    public TaskPaneTaskBar() {
    }

    public void createTaskRectangle(UIControl uiControl,
                                    ITask task) {
        Rectangle backgroundRectangle = new Rectangle();
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
        //ширина зависит от ширины backgroundRectangle и task.donePercent
        donePercentRectangle.widthProperty().bind(
                backgroundRectangle.widthProperty().divide(100).multiply(task.donePercentProperty()));
        //также привязываем все ивенты от backgroundRectangle
        donePercentRectangle.onMousePressedProperty().bind(backgroundRectangle.onMousePressedProperty());
        donePercentRectangle.onMouseDraggedProperty().bind(backgroundRectangle.onMouseDraggedProperty());

        this.getChildren().add(backgroundRectangle);
        this.getChildren().add(donePercentRectangle);

        enableDrag(uiControl, task, backgroundRectangle, uiControl.getZoomMultiplier());
        enableResize(uiControl, task, backgroundRectangle, uiControl.getZoomMultiplier());

        setListeners(uiControl, task, backgroundRectangle, donePercentRectangle);
    }

    private void setParameters(UIControl uiControl,
                               ITask task,
                               Rectangle backgroundRectangle) {
        backgroundRectangle.setWidth(taskWidth(task.getStartDate(), task.getFinishDate(), uiControl.getZoomMultiplier()));
        this.setLayoutX(taskX(task.getStartDate(), uiControl.getController().getProject().getStartDate(), uiControl.getZoomMultiplier()));
        this.setLayoutY(taskY(uiControl.getController().getProject().getTaskList().indexOf(task)));
    }

    private double taskWidth(LocalDate taskStartDate,
                             LocalDate taskFinishDate,
                             int columnWidth) {
        return (ChronoUnit.DAYS.between(taskStartDate, taskFinishDate) * columnWidth);
    }

    private double taskY(int rowIndex) {
        this.rowIndex = rowIndex;
        return (rowIndex * rowHeight) + 6.5;
    }

    private double taskX(LocalDate taskStartDate,
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
    private Timeline createTimelineAnimation(DoubleProperty target, double endValue, double duration) {
        KeyValue endKeyValue = new KeyValue(target, endValue, Interpolator.SPLINE(0.4, 0, 0.2, 1));
        KeyFrame endKeyFrame = new KeyFrame(Duration.millis(duration), endKeyValue);
        return new Timeline(endKeyFrame);
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
                    // Анимация при удалиении и добавления элемента на диаграмме
                    Timeline timeline = createTimelineAnimation(
                            this.layoutYProperty(),
                            taskY(uiControl.getController().getProject().getTaskList().indexOf(task)),
                            (rowIndex + 1) * 150);
                    timeline.play();
                }
            }
        });
        // Следим за изменением начальной и конечной даты
        task.startDateProperty().addListener((observable, oldValue, newValue) -> {
            if (!wasMoved) {
                // Animation
                Timeline timeline = createTimelineAnimation(
                        this.layoutXProperty(),
                        taskX(task.getStartDate(), uiControl.getController().getProject().getStartDate(), uiControl.getZoomMultiplier()),
                        400);
                timeline.play();
                /* При изменении начальной даты через свойства, также двигаем конечную дату.
                Длина прямоугольника при изменении начальной даты должна оставаться неизменной.
                Также task.setFinishDate(...) оборачиваем в wasMoved, чтобы не сработал листенер конечной даты */
                wasMoved = true; // Когда начитаем двигать, то тру, чтобы не началась рекурсия
                task.setFinishDate(task.getStartDate().plusDays(Math.round(backgroundRectangle.getWidth() / uiControl.getZoomMultiplier())));
                wasMoved = false; // Когда окончили движение фолз
            }
        });
        task.finishDateProperty().addListener((observable, oldValue, newValue) -> {
            if (!wasMoved) {
                // Animation
                Timeline timeline = createTimelineAnimation(
                        backgroundRectangle.widthProperty(),
                        taskWidth(task.getStartDate(), task.getFinishDate(), uiControl.getZoomMultiplier()),
                        400);
                timeline.play();
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

    public void setContextMenu(IController controller,
                               ITask task) {
        taskContextMenu = new TaskContextMenu();
        taskContextMenu.initMenus(controller, task);
    }

    public void setTooltip(TaskTooltip taskTooltip) {
        Tooltip.install(this, taskTooltip);
    }

    /**
     * Метод который включает возможность перемещения метки с помощью мышки по оси Х
     *
     * @param uiControl           Контроллер интерфейса
     * @param task                Задача этой метки
     * @param backgroundRectangle Прямоугольник
     * @param columnWidth         Ширина дня в пикселях (uiControl.getZoomMultiplier())
     */
    private void enableDrag(UIControl uiControl,
                            ITask task,
                            Rectangle backgroundRectangle,
                            int columnWidth) {
        final Delta dragDelta = new Delta();
        final OldRound oldRound = new OldRound();
        backgroundRectangle.setOnMousePressed(event -> {
            // Выделяем нужный элемент в таблице
            uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController().getTaskTreeTableView().getSelectionModel().select(rowIndex);
            if (event.isPrimaryButtonDown()) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = getLayoutX() - event.getSceneX();
                getScene().setCursor(Cursor.MOVE);
            }
            // Условие для контекстного меню
            if (event.isSecondaryButtonDown()) {
                taskContextMenu.show(TaskPaneTaskBar.this, event.getScreenX(), event.getScreenY());
            }
        });
        backgroundRectangle.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                double newX = event.getSceneX() + dragDelta.x;
                if (newX > 0 && newX + backgroundRectangle.getWidth() <= this.getParent().getBoundsInParent().getWidth()) {
                    // Хреначим привязку к сетке
                    if (Math.round(newX / columnWidth) != oldRound.old) {
                        oldRound.old = Math.round(newX / columnWidth);
                        this.setLayoutX(Math.round(newX / columnWidth) * columnWidth - 2);
                        wasMoved = true; // Когда начитаем двигать, то тру, чтобы не началась рекурсия
                        task.setStartDate(uiControl.getController().getProject().getStartDate().plusDays(Math.round(newX / columnWidth)));
                        task.setFinishDate(task.getStartDate().plusDays(Math.round(backgroundRectangle.getWidth() / columnWidth)));
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
     * @param uiControl           Контроллер интерфейса
     * @param task                Задача этой метки
     * @param backgroundRectangle Прямоугольник
     * @param columnWidth         Ширина дня в пикселях (uiControl.getZoomMultiplier())
     */
    public void enableResize(UIControl uiControl,
                             ITask task,
                             Rectangle backgroundRectangle,
                             int columnWidth) {
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
            uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController().getTaskTreeTableView().getSelectionModel().select(rowIndex);
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
                            this.setLayoutX(Math.round(newX / columnWidth) * columnWidth - 2);
                            backgroundRectangle.setWidth(backgroundRectangle.getWidth() - (this.getLayoutX() - oldX));
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
        rightResizeHandle.xProperty().bind(backgroundRectangle.xProperty().add(backgroundRectangle.widthProperty()).subtract(rightResizeHandle.widthProperty()));
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
            uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController().getTaskTreeTableView().getSelectionModel().select(rowIndex);
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
                if (newWidth >= columnWidth && this.getLayoutX() + newWidth <= this.getParent().getBoundsInLocal().getWidth()) {
                    /** Хреначим привязку к сетке */
                    if (Math.round(newWidth / columnWidth) != oldRoundRight.old) {
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
}
