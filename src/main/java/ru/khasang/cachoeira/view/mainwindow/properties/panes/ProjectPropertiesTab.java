package ru.khasang.cachoeira.view.mainwindow.properties.panes;

import javafx.scene.image.ImageView;

public class ProjectPropertiesTab extends AbstractPropertiesTab {
    public ProjectPropertiesTab() {
        this.setText("Project");
        this.setGraphic(new ImageView(getClass().getResource("/img/ic_project.png").toExternalForm()));
    }
}
