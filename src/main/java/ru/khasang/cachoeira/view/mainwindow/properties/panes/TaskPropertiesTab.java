package ru.khasang.cachoeira.view.mainwindow.properties.panes;

import javafx.scene.image.ImageView;

public class TaskPropertiesTab extends AbstractPropertiesTab {
    public TaskPropertiesTab() {
        this.setText("Task");
        this.setGraphic(new ImageView(getClass().getResource("/img/ic_task.png").toExternalForm()));
    }
}
