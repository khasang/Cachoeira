package ru.khasang.cachoeira.view.mainwindow.properties.modules;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ru.khasang.cachoeira.model.ITask;

import java.util.Arrays;

public class ResourceAssignedTasks extends TableView<ITask> {
    private TableColumn<ITask, String> taskNameColumn;
    private TableColumn<ITask, Boolean> taskCheckboxColumn;

    public ResourceAssignedTasks() {
        taskNameColumn = new TableColumn<>("Name");
        taskCheckboxColumn = new TableColumn<>();
    }

    public void createPane() {
        taskNameColumn.setPrefWidth(251);
        taskCheckboxColumn.setPrefWidth(47);

        this.getColumns().addAll(Arrays.asList(taskNameColumn, taskCheckboxColumn));
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
}
