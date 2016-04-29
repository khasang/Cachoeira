package ru.khasang.cachoeira.view.mainwindow.diagram.tables;

import javafx.beans.property.DoubleProperty;

public class ResourceTableView<S> extends AbstractTableView<S> {

    public ResourceTableView(DoubleProperty horizontalScrollValue, DoubleProperty verticalScrollValue) {
        this.horizontalScrollValue = horizontalScrollValue;
        this.verticalScrollValue = verticalScrollValue;
    }
}
