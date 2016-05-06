package ru.khasang.cachoeira.view.mainwindow.properties.modules;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ru.khasang.cachoeira.model.IResource;

import java.util.Arrays;

public class TaskAssignedResources extends TableView<IResource> implements IModule {
    private TableColumn<IResource, String> resourceNameColumn;
    private TableColumn<IResource, Boolean> resourceCheckboxColumn;

    public TaskAssignedResources() {
        resourceNameColumn = new TableColumn<>("Name");
        resourceCheckboxColumn = new TableColumn<>();
    }

    @Override
    public void createPane() {
        resourceNameColumn.setPrefWidth(251);
        resourceCheckboxColumn.setPrefWidth(47);

        this.getColumns().addAll(Arrays.asList(resourceNameColumn, resourceCheckboxColumn));
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public TableColumn<IResource, String> getResourceNameColumn() {
        return resourceNameColumn;
    }

    public TableColumn<IResource, Boolean> getResourceCheckboxColumn() {
        return resourceCheckboxColumn;
    }
}
