package ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.ganttchart;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.GanttChart;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.layers.ObjectsChartLayer;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.TaskBar;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.TaskGanttPlanTaskBar;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.layers.BarLayerFactoryImpl;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;

public abstract class GanttChartController {
    protected final GanttChart ganttChart;
    protected final MainWindowController controller;

    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<ITask> taskListChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener startDateListener;
    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener finishDateListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<ITask> taskChangeListener;

    public GanttChartController(GanttChart ganttChart, MainWindowController controller) {
        this.ganttChart = ganttChart;
        this.controller = controller;
    }

    public void attachEvents() {
        bindScrolls();

        taskListChangeListener = this::taskListChangeHandler;
        startDateListener = this::startDateChangeHandler;
        finishDateListener = this::finishDateChangeHandler;
        taskChangeListener = this::taskChangeHandler;

        controller.getProject().getTaskList().addListener(taskListChangeListener);
        controller.getProject().startDateProperty().addListener(startDateListener);
        controller.getProject().finishDateProperty().addListener(finishDateListener);
        controller.selectedTaskProperty().addListener(taskChangeListener);
    }

    private void taskChangeHandler(ObservableValue<? extends ITask> observableValue, ITask oldTask, ITask newTask) {
        ObjectsChartLayer objectsChartLayer = (ObjectsChartLayer) ganttChart.getObjectsChartLayer();
        if (oldTask != null) {
            TaskBar oldTaskBar = objectsChartLayer.findTaskBarByTask(oldTask);
            if (oldTaskBar != null) {
                oldTaskBar.setSelected(false);
            }
        }
        if (newTask != null) {
            TaskBar newTaskBar = objectsChartLayer.findTaskBarByTask(newTask);
            if (newTaskBar != null) {
                newTaskBar.setSelected(true);
            }
        }
    }

    private void taskListChangeHandler(ListChangeListener.Change<? extends ITask> change) {
        while (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(this::createTaskBar);
            }
            if (change.wasRemoved()) {
                change.getRemoved().forEach(this::removeTaskBar);
            }
        }
    }

    private void createTaskBar(ITask task) {
        ObjectsChartLayer objectsChartLayer = (ObjectsChartLayer) ganttChart.getObjectsChartLayer();
        TaskBar taskBar = new TaskGanttPlanTaskBar(task, new BarLayerFactoryImpl(), controller);
        taskBar.createBar();
        taskBar.setSelected(true);
        objectsChartLayer.addTaskBar(taskBar);
    }

    private void removeTaskBar(ITask task) {
        ObjectsChartLayer objectsChartLayer = (ObjectsChartLayer) ganttChart.getObjectsChartLayer();
        objectsChartLayer.removeTaskBar(task);
    }

    protected abstract void startDateChangeHandler(Observable observable);

    protected abstract void finishDateChangeHandler(Observable observable);

    protected abstract void bindScrolls();

}
