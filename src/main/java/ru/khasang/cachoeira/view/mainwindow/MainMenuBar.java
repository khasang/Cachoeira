package ru.khasang.cachoeira.view.mainwindow;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class MainMenuBar extends MenuBar {
    private MenuItem createProject;
    private MenuItem openProject;
    private MenuItem saveProject;
    private MenuItem saveAsProject;
    private MenuItem exportResources;
    private MenuItem importResources;
    private MenuItem exit;
    private MenuItem undo;
    private MenuItem redo;
    private MenuItem about;

    public MainMenuBar() {
    }

    public void createMenu() {
        this.getMenus().addAll(createProjectMenu(), createEditMenu(), createHelpMenu());
    }

    private Menu createProjectMenu() {
        createProject = new MenuItem("Create");
        createProject.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        openProject = new MenuItem("Open");
        openProject.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        saveProject = new MenuItem("Save");
        saveProject.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveAsProject = new MenuItem("Save as");
        saveAsProject.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN));
        Menu resourcesMenu = new Menu("Resources");
        exportResources = new MenuItem("Export");
        importResources = new MenuItem("Import");
        resourcesMenu.getItems().addAll(exportResources, importResources);
        exit = new MenuItem("Exit");
        Menu projectMenu = new Menu("Project");
        projectMenu.getItems().addAll(createProject, openProject, saveProject, saveAsProject, resourcesMenu, new SeparatorMenuItem(), exit);
        return projectMenu;
    }

    private Menu createEditMenu() {
        undo = new MenuItem("Undo");
        undo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        redo = new MenuItem("Redo");
        redo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN));
        Menu editMenu = new Menu("Edit");
        editMenu.getItems().addAll(undo, redo);
        return editMenu;
    }

    private Menu createHelpMenu() {
        about = new MenuItem("About");
        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().addAll(about);
        return helpMenu;
    }

    public MenuItem getCreateProject() {
        return createProject;
    }

    public MenuItem getOpenProject() {
        return openProject;
    }

    public MenuItem getSaveProject() {
        return saveProject;
    }

    public MenuItem getSaveAsProject() {
        return saveAsProject;
    }

    public MenuItem getExportResources() {
        return exportResources;
    }

    public MenuItem getImportResources() {
        return importResources;
    }

    public MenuItem getExit() {
        return exit;
    }

    public MenuItem getUndo() {
        return undo;
    }

    public MenuItem getRedo() {
        return redo;
    }

    public MenuItem getAbout() {
        return about;
    }
}
