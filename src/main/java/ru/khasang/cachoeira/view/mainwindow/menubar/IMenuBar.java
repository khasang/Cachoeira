package ru.khasang.cachoeira.view.mainwindow.menubar;

import javafx.scene.control.MenuItem;

public interface IMenuBar {
    void createMenu();

    MenuItem getCreateProject();

    MenuItem getOpenProject();

    MenuItem getSaveProject();

    MenuItem getSaveAsProject();

    MenuItem getExportResources();

    MenuItem getImportResources();

    MenuItem getExit();

    MenuItem getUndo();

    MenuItem getRedo();

    MenuItem getAbout();
}
