package ru.khasang.cachoeira.view.mainwindow;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import ru.khasang.cachoeira.model.IProject;

public class MainWindowView {
    private final IProject project;

    public MainWindowView(IProject project) {
        this.project = project;
    }

    public void createView() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createMenuBar());
        borderPane.setCenter(createGanttPlanLayout());
        borderPane.setRight(createPropertiesMenu());
    }

    private Node createPropertiesMenu() {
        return null;
    }

    private Node createGanttPlanLayout() {
        return null;
    }

    private Node createMenuBar() {
        MainMenuBar mainMenuBar = new MainMenuBar();
        mainMenuBar.createMenu();
        return mainMenuBar;
    }
}
