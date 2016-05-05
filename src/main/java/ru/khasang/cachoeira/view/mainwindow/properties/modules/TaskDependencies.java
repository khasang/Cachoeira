package ru.khasang.cachoeira.view.mainwindow.properties.modules;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ru.khasang.cachoeira.model.IDependentTask;
import ru.khasang.cachoeira.model.TaskDependencyType;

import java.util.Arrays;

public class TaskDependencies extends TableView<IDependentTask> {
    private TableColumn<IDependentTask, String> parentTaskNameColumn;
    private TableColumn<IDependentTask, TaskDependencyType> parentTaskDependencyTypeColumn;

    public void createPane() {
        parentTaskNameColumn = new TableColumn<>("Name");
        parentTaskDependencyTypeColumn = new TableColumn<>("Dependency Type");

        this.getColumns().addAll(Arrays.asList(parentTaskNameColumn, parentTaskDependencyTypeColumn));
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public TableColumn<IDependentTask, String> getParentTaskNameColumn() {
        return parentTaskNameColumn;
    }

    public TableColumn<IDependentTask, TaskDependencyType> getParentTaskDependencyTypeColumn() {
        return parentTaskDependencyTypeColumn;
    }
}
