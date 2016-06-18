package ru.khasang.cachoeira.viewcontroller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.scene.control.TreeItem;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.Resource;
import ru.khasang.cachoeira.model.Task;
import ru.khasang.cachoeira.view.mainwindow.diagram.tables.AbstractTableView;
import ru.khasang.cachoeira.view.mainwindow.diagram.tables.ResourceTableView;
import ru.khasang.cachoeira.view.mainwindow.diagram.tables.TaskTableView;
import ru.khasang.cachoeira.view.mainwindow.properties.AbstractPropertiesSidePanel;
import ru.khasang.cachoeira.viewcontroller.draganddrop.ResourceTableDragAndDrop;
import ru.khasang.cachoeira.viewcontroller.draganddrop.TaskTableDragAndDrop;
import ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.PropertiesSidePanelController;
import ru.khasang.cachoeira.viewcontroller.rowfactories.ResourceTreeTableViewRowFactory;
import ru.khasang.cachoeira.viewcontroller.rowfactories.TaskTreeTableViewRowFactory;

/**
 * Implementation of MainWindowFactory.
 */
public class MainWindowFactoryImpl implements MainWindowFactory {
    private MainWindowController controller;

    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<ITask> taskListChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<TreeItem<ITask>> taskChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<TreeItem<ITask>> taskTreeItemsChangeListener;

    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<IResource> resourceListChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<TreeItem<IResource>> resourceChangeListener;
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<TreeItem<IResource>> resourceTreeItemsChangeListener;

    public MainWindowFactoryImpl(MainWindowController controller) {
        this.controller = controller;
    }

    @Override
    public AbstractTableView<ITask> createTaskTable(DoubleProperty horizontalScrollValue,
                                                    DoubleProperty verticalScrollValue) {
        AbstractTableView<ITask> taskTableView = new TaskTableView<>(horizontalScrollValue, verticalScrollValue);

        taskTableView.setRoot(new TreeItem<>(new Task()));
        taskTableView.setRowFactory(new TaskTreeTableViewRowFactory(controller, new TaskTableDragAndDrop(controller)));
        taskListChangeListener = this::taskChangeListenerHandler;
        controller.getProject().getTaskList().addListener(new WeakListChangeListener<>(taskListChangeListener));

        taskChangeListener = this::refreshSelectedTask;
        taskTableView.getSelectionModel().selectedItemProperty()
                .addListener(new WeakChangeListener<>(taskChangeListener));

        taskTreeItemsChangeListener = this::setSelectedTaskNull;
        taskTableView.getRoot().getChildren().addListener(new WeakListChangeListener<>(taskTreeItemsChangeListener));
        return taskTableView;
    }

    @Override
    public AbstractTableView<IResource> createResourceTable(DoubleProperty horizontalScrollValue,
                                                            DoubleProperty verticalScrollValue) {
        AbstractTableView<IResource> resourceTableView = new ResourceTableView<>(horizontalScrollValue, verticalScrollValue);

        resourceTableView.setRoot(new TreeItem<>(new Resource()));
        resourceTableView.setRowFactory(new ResourceTreeTableViewRowFactory(controller, new ResourceTableDragAndDrop(controller)));
        resourceListChangeListener = this::resourceChangeListenerHandler;
        controller.getProject().getResourceList().addListener(new WeakListChangeListener<>(resourceListChangeListener));

        resourceChangeListener = this::refreshSelectedResource;
        resourceTableView.getSelectionModel().selectedItemProperty().addListener(new WeakChangeListener<>(resourceChangeListener));

        resourceTreeItemsChangeListener = this::setSelectedResourceNull;
        resourceTableView.getRoot().getChildren().addListener(new WeakListChangeListener<>(resourceTreeItemsChangeListener));
        return resourceTableView;
    }

    @Override
    public AbstractPropertiesSidePanel createSidePropertiesPanel() {
        PropertiesSidePanelController sidePanelController = new PropertiesSidePanelController(controller);
        sidePanelController.createSidePanel();
        return sidePanelController.getSidePanel();
    }

    private void refreshSelectedTask(ObservableValue<? extends TreeItem<ITask>> observableValue,
                                     TreeItem<ITask> oldTaskTreeItem,
                                     TreeItem<ITask> newTaskTreeItem) {
        if (newTaskTreeItem != null) {
            controller.selectedTaskProperty().setValue(newTaskTreeItem.getValue());
        }
    }

    private void setSelectedTaskNull(ListChangeListener.Change<? extends TreeItem<ITask>> change) {
        while (change.next()) {
            if (change.getList().isEmpty()) {
                controller.selectedTaskProperty().setValue(null);
            }
        }
    }

    private void taskChangeListenerHandler(ListChangeListener.Change<? extends ITask> change) {
        // При добавлении или удалении элемента из модели обновляем таблицу задач
        while (change.next()) {
            // Добавляем
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(this::createTreeItem);
            }
            // Удаляем
            if (change.wasRemoved()) {
                change.getRemoved().forEach(task ->
                        removeTreeItem(task, controller.getTaskTableView().getRoot().getChildren()));
            }
        }
    }

    private void createTreeItem(ITask task) {
        // Получаем индекс задачи
        int indexOfTask = controller.getProject().getTaskList().indexOf(task);
        // Создаем элемент таблицы и присваиваем ему нашу задачу
        TreeItem<ITask> taskTreeItem = new TreeItem<>(task);
        // Добавляем элемент в таблицу на нужную строку (indexOfTask)
        // Обязательно нужен индекс элемента, иначе драгндроп не будет работать
        controller.getTaskTableView().getRoot().getChildren().add(indexOfTask, taskTreeItem);
        // Выделяем добавленный элемент в таблице
        controller.getTaskTableView().getSelectionModel().select(taskTreeItem);
    }

    private void setSelectedResourceNull(ListChangeListener.Change<? extends TreeItem<IResource>> change) {
        while (change.next()) {
            if (change.getList().isEmpty()) {
                controller.selectedResourceProperty().setValue(null);
            }
        }
    }

    private void refreshSelectedResource(ObservableValue<? extends TreeItem<IResource>> observableValue,
                                         TreeItem<IResource> oldResourceTreeItem,
                                         TreeItem<IResource> newResourceTreeItem) {
        if (newResourceTreeItem != null) {
            controller.selectedResourceProperty().setValue(newResourceTreeItem.getValue());
        }
    }

    private void resourceChangeListenerHandler(ListChangeListener.Change<? extends IResource> change) {
        // При добавлении или удалении элемента из модели обновляем таблицу задач
        while (change.next()) {
            // Добавляем
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(resource -> {
                    // Получаем индекс задачи
                    int indexOfTask = controller.getProject().getResourceList().indexOf(resource);
                    // Создаем элемент таблицы и присваиваем ему нашу задачу
                    TreeItem<IResource> resourceTreeItem = new TreeItem<>(resource);
                    // Добавляем элемент в таблицу на нужную строку (indexOfTask)
                    // Обязательно нужен индекс элемента, иначе драгндроп не будет работать
                    controller.getResourceTableView().getRoot().getChildren().add(indexOfTask, resourceTreeItem);
                    // Выделяем добавленный элемент в таблице
                    controller.getResourceTableView().getSelectionModel().select(resourceTreeItem);
                });
            }
            // Удаляем
            if (change.wasRemoved()) {
                change.getRemoved().forEach(resource -> {
                    // Сначала удаляем из таблицы...
                    removeTreeItem(resource, controller.getResourceTableView().getRoot().getChildren());
                });
            }
        }
    }

    /**
     * Removes item from list.
     *
     * @param object   Object which should be removed (task or resource).
     * @param itemList Table item list.
     * @param <T>      Object type (ITask or IResource).
     */
    public <T> void removeTreeItem(T object, ObservableList<TreeItem<T>> itemList) {
        itemList.removeIf(item -> item.getValue().equals(object));
    }
}
