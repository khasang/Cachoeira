package ru.khasang.cachoeira.view.mainwindow.diagram.tables;

import javafx.beans.property.DoubleProperty;

public class TaskTableView<S> extends AbstractTableView<S> {
    public TaskTableView(DoubleProperty horizontalScrollValue, DoubleProperty verticalScrollValue) {
        this.horizontalScrollValue = horizontalScrollValue;
        this.verticalScrollValue = verticalScrollValue;
    }
}
