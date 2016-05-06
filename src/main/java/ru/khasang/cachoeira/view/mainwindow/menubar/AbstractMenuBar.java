package ru.khasang.cachoeira.view.mainwindow.menubar;

import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public abstract class AbstractMenuBar extends MenuBar{
    public abstract void createMenu();

    public abstract MenuItem getCreateProject();

    public abstract MenuItem getOpenProject();

    public abstract MenuItem getSaveProject();

    public abstract MenuItem getSaveAsProject();

    public abstract MenuItem getExportResources();

    public abstract MenuItem getImportResources();

    public abstract MenuItem getExit();

    public abstract MenuItem getUndo();

    public abstract MenuItem getRedo();

    public abstract MenuItem getAbout();
}
