package ru.khasang.cachoeira.view.mainwindow.properties.panes;

import javafx.scene.image.ImageView;

public class ResourcePropertiesTab extends AbstractPropertiesTab {
    public ResourcePropertiesTab() {
        this.setText("Resource");
        this.setGraphic(new ImageView(getClass().getResource("/img/ic_resource.png").toExternalForm()));
    }
}
