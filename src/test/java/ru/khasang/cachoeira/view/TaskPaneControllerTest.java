package ru.khasang.cachoeira.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.junit.Assert;
import org.junit.Test;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.Task;

public class TaskPaneControllerTest {

    @Test
    public void testRemoveTaskTreeItem() throws Exception {
        TaskPaneController taskPaneController = new TaskPaneController();
        ITask task1 = new Task();
        ITask task2 = new Task();
        ITask task3 = new Task();
        TreeItem<ITask> taskTreeItem1 = new TreeItem<>(task1);
        TreeItem<ITask> taskTreeItem2 = new TreeItem<>(task2);
        TreeItem<ITask> taskTreeItem3 = new TreeItem<>(task3);

        ObservableList<TreeItem<ITask>> list1 = FXCollections.observableArrayList();
        list1.addAll(taskTreeItem1, taskTreeItem2, taskTreeItem3);
        ObservableList<TreeItem<ITask>> list2 = FXCollections.observableArrayList();
        list2.addAll(taskTreeItem1, taskTreeItem3);

        taskPaneController.removeTaskTreeItem(task2, list1);
        Assert.assertEquals(list2, list1);

        taskPaneController.removeTaskTreeItem(task1, list1);
        list2.remove(taskTreeItem1);
        Assert.assertEquals(list2, list1);

        taskPaneController.removeTaskTreeItem(task3, list1);
        list2.remove(taskTreeItem3);
        Assert.assertEquals(list2, list1);
    }
}