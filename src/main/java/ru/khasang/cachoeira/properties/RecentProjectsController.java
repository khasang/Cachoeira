package ru.khasang.cachoeira.properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;

public class RecentProjectsController {
    private static RecentProjectsController instance;

    private ObservableList<File> recentProjects;

    private RecentProjectsController() {
        recentProjects = FXCollections.observableArrayList(SettingsManager.getInstance().getRecentProjects());
    }

    public static RecentProjectsController getInstance() {
        if (instance == null) {
            instance = new RecentProjectsController();
        }
        return instance;
    }

    public ObservableList<File> getRecentProjects() {
        return recentProjects;
    }

    public void addRecentProject(File file) {
        if (!recentProjects.contains(file)) {
            recentProjects.add(0, file);
        } else {
            recentProjects.remove(file);
            recentProjects.add(0, file);
        }
    }

    public void removeRecentProject(File file) {
        recentProjects.remove(file);
    }
}
