package ru.khasang.cachoeira.vcontroller.rowfactories;

import javafx.scene.control.Alert;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import ru.khasang.cachoeira.data.DataService;
import ru.khasang.cachoeira.properties.RecentProjectsController;
import ru.khasang.cachoeira.properties.SettingsManager;
import ru.khasang.cachoeira.vcontroller.StartWindowController;

import java.io.File;

public class RecentProjectsRowFactory implements Callback<TableView<File>, TableRow<File>> {
    private StartWindowController controller;

    public RecentProjectsRowFactory(StartWindowController controller) {
        this.controller = controller;
    }

    @Override
    public TableRow<File> call(TableView<File> param) {
        TableRow<File> row = new TableRow<>();
        row.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2 && !row.isEmpty()) {
                File recentProject = row.getItem();
                if (recentProject.exists()) {
                    DataService.getInstance().loadProject(recentProject);
                    if (controller.getView().getStage().isShowing()) {
                        controller.getView().getStage().close(); //закрываем стартовое окно
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "File does not exist");
                    alert.showAndWait();
                    RecentProjectsController.getInstance().removeRecentProject(recentProject);
                    SettingsManager.getInstance().writeRecentProjects(RecentProjectsController.getInstance().getRecentProjects());
                }
            }
        });
        return row;
    }
}
