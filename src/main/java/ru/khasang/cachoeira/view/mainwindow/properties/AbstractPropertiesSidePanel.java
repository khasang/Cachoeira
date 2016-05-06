package ru.khasang.cachoeira.view.mainwindow.properties;

import javafx.scene.control.TabPane;
import ru.khasang.cachoeira.view.mainwindow.properties.panes.AbstractPropertiesPane;

public abstract class AbstractPropertiesSidePanel extends TabPane {
    public abstract void createPanes();

    public abstract AbstractPropertiesPane getProjectPropertiesPane();

    public abstract AbstractPropertiesPane getTaskPropertiesPane();

    public abstract AbstractPropertiesPane getResourcePropertiesPane();
}
