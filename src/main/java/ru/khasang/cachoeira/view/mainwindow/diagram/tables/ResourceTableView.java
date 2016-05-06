package ru.khasang.cachoeira.view.mainwindow.diagram.tables;

import javafx.beans.property.DoubleProperty;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ResourceType;

import java.util.Arrays;

public class ResourceTableView<S> extends AbstractTableView<S> {

    public ResourceTableView(DoubleProperty horizontalScrollValue, DoubleProperty verticalScrollValue) {
        this.horizontalScrollValue = horizontalScrollValue;
        this.verticalScrollValue = verticalScrollValue;
    }

    @Override
    public void createTable() {
        TreeTableColumn<S, String> resourceNameColumn = new TreeTableColumn<>("Name");
        TreeTableColumn<S, ResourceType> resourceTypeColumn = new TreeTableColumn<>("Type");
        TreeTableColumn<S, String> resourceEmailColumn = new TreeTableColumn<>("E-Mail");

        resourceNameColumn.setCellValueFactory(cellData -> {
            IResource resource = (IResource) cellData.getValue().getValue();
            return resource.nameProperty();
        });
        resourceTypeColumn.setCellValueFactory(cellData -> {
            IResource resource = (IResource) cellData.getValue().getValue();
            return resource.resourceTypeProperty();
        });
        resourceEmailColumn.setCellValueFactory(cellData -> {
            IResource resource = (IResource) cellData.getValue().getValue();
            return resource.emailProperty();
        });

        resourceEmailColumn.setVisible(false);

        resourceNameColumn.setStyle("-fx-alignment: CENTER-LEFT");
        resourceTypeColumn.setStyle("-fx-alignment: CENTER-LEFT");
        resourceEmailColumn.setStyle("-fx-alignment: CENTER-LEFT");

        this.getColumns().addAll(Arrays.asList(resourceNameColumn, resourceTypeColumn, resourceEmailColumn));
        this.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

        this.setShowRoot(false);
    }
}
