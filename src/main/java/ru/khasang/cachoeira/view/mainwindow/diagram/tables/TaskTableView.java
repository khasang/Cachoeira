package ru.khasang.cachoeira.view.mainwindow.diagram.tables;

import javafx.beans.property.DoubleProperty;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.PriorityType;

import java.time.LocalDate;
import java.util.Arrays;

public class TaskTableView<S> extends AbstractTableView<S> {
    public TaskTableView(DoubleProperty horizontalScrollValue, DoubleProperty verticalScrollValue) {
        this.horizontalScrollValue = horizontalScrollValue;
        this.verticalScrollValue = verticalScrollValue;
    }

    @Override
    public void createTable() {
        TreeTableColumn<S, String> taskNameColumn = new TreeTableColumn<>("Name");
        TreeTableColumn<S, LocalDate> taskStartDateColumn = new TreeTableColumn<>("Start Date");
        TreeTableColumn<S, LocalDate> taskFinishDateColumn = new TreeTableColumn<>("Finish Date");
        TreeTableColumn<S, Integer> taskDurationColumn = new TreeTableColumn<>("Duration");
        TreeTableColumn<S, Integer> taskDonePercentColumn = new TreeTableColumn<>("Progress");
        TreeTableColumn<S, PriorityType> taskPriorityColumn = new TreeTableColumn<>("Priority");
        TreeTableColumn<S, Double> taskCostColumn = new TreeTableColumn<>("Cost");

        taskNameColumn.setCellValueFactory(cellData -> {
            ITask task = (ITask) cellData.getValue().getValue();
            return task.nameProperty();
        });
        taskStartDateColumn.setCellValueFactory(cellData -> {
            ITask task = (ITask) cellData.getValue().getValue();
            return task.startDateProperty();
        });
        taskFinishDateColumn.setCellValueFactory(cellData -> {
            ITask task = (ITask) cellData.getValue().getValue();
            return task.finishDateProperty();
        });
        taskDurationColumn.setCellValueFactory(cellData -> {
            ITask task = (ITask) cellData.getValue().getValue();
            return task.durationProperty().asObject();
        });
        taskDonePercentColumn.setCellValueFactory(cellData -> {
            ITask task = (ITask) cellData.getValue().getValue();
            return task.donePercentProperty().asObject();
        });
        taskPriorityColumn.setCellValueFactory(cellData -> {
            ITask task = (ITask) cellData.getValue().getValue();
            return task.priorityTypeProperty();
        });
        taskCostColumn.setCellValueFactory(cellData -> {
            ITask task = (ITask) cellData.getValue().getValue();
            return task.costProperty().asObject();
        });

        taskDurationColumn.setVisible(false);
        taskPriorityColumn.setVisible(false);
        taskDonePercentColumn.setVisible(false);
        taskCostColumn.setVisible(false);

        taskNameColumn.setStyle("-fx-alignment: CENTER-LEFT");
        taskDurationColumn.setStyle("-fx-alignment: CENTER-LEFT");
        taskDonePercentColumn.setStyle("-fx-alignment: CENTER-LEFT");
        taskPriorityColumn.setStyle("-fx-alignment: CENTER-LEFT");
        taskCostColumn.setStyle("-fx-alignment: CENTER-LEFT");

        this.getColumns().addAll(Arrays.asList(taskNameColumn, taskStartDateColumn, taskFinishDateColumn, taskDurationColumn, taskDonePercentColumn, taskPriorityColumn, taskCostColumn));
        this.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

        this.setShowRoot(false);
    }
}
