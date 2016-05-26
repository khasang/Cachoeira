package ru.khasang.cachoeira.view.mainwindow.properties;

import javafx.scene.control.TabPane;
import ru.khasang.cachoeira.view.mainwindow.properties.panes.AbstractPropertiesTab;

public abstract class AbstractPropertiesSidePanel extends TabPane {
    public abstract void createPanes();

    public abstract AbstractPropertiesTab getProjectPropertiesTab();

    public abstract AbstractPropertiesTab getTaskPropertiesTab();

    public abstract AbstractPropertiesTab getResourcePropertiesTab();
}
