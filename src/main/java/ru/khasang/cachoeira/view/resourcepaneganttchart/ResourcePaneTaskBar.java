package ru.khasang.cachoeira.view.resourcepaneganttchart;

import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;
import ru.khasang.cachoeira.view.tooltips.TaskTooltip;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created by truesik on 04.11.2015.
 */
public class ResourcePaneTaskBar extends StackPane {
    private static final double TASK_HEIGHT = 18;   //высота прямоугольника задачи
    private static final double rowHeight = 31;

    private ITask task;
    private IResource resource;
    private int columnWidth;
    private boolean wasMoved;
    private ContextMenu contextMenu;

    public ResourcePaneTaskBar(int columnWidth) {
        this.columnWidth = columnWidth;
        setAlignment(Pos.CENTER_LEFT);
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
        donePercentRectangle.heightProperty().bind(backgroundRectangle.heightProperty().subtract(2.5));
        donePercentRectangle.widthProperty().bind(
                backgroundRectangle.widthProperty().divide(100).multiply(task.donePercentProperty()));

        this.getChildren().add(backgroundRectangle);
        this.getChildren().add(donePercentRectangle);

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
                    this.setLayoutY(taskY(uiControl.getController().getProject().getResourceList().indexOf(resource)));
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
                           ITask task) {
        final Delta dragDelta = new Delta();
        final OldRound oldRound = new OldRound();
        setOnMousePressed(event -> {
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
                contextMenu.show(ResourcePaneTaskBar.this, event.getScreenX(), event.getScreenY());
            }
        });
        setOnMouseDragged(event -> {
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
        setOnMouseExited(event -> {
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
