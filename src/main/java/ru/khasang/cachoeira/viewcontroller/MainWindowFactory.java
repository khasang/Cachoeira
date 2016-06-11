package ru.khasang.cachoeira.viewcontroller;

import javafx.beans.property.DoubleProperty;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.mainwindow.diagram.tables.AbstractTableView;
import ru.khasang.cachoeira.view.mainwindow.properties.AbstractPropertiesSidePanel;

public interface MainWindowFactory {
    /**
     * Creates table view with tasks.
     *
     * @param horizontalScrollValue Double property value which binds with table horizontal scroll.
     * @param verticalScrollValue   Double property value which binds with table vertical scroll.
     * @return Returns table view.
     */
    AbstractTableView<ITask> createTaskTable(DoubleProperty horizontalScrollValue,
                                             DoubleProperty verticalScrollValue);

    /**
     * Creates table view with resources.
     *
     * @param horizontalScrollValue Double property value which binds with table horizontal scroll.
     * @param verticalScrollValue   Double property value which binds with table vertical scroll.
     * @return Returns table view.
     */
    AbstractTableView<IResource> createResourceTable(DoubleProperty horizontalScrollValue,
                                                     DoubleProperty verticalScrollValue);

    /**
     * Creates side panel with properties.
     *
     * @return Returns properties panel.
     */
    AbstractPropertiesSidePanel createSidePropertiesPanel();
}
