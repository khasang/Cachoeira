package ru.khasang.cachoeira.viewcontroller.mainwindowcontrollers.propertiesmodules;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.cell.ComboBoxTableCell;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.model.TaskDependencyType;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;
import ru.khasang.cachoeira.viewcontroller.contextmenus.ParentTaskContextMenu;
import ru.khasang.cachoeira.view.mainwindow.properties.modules.IModule;
import ru.khasang.cachoeira.view.mainwindow.properties.modules.TaskDependencies;

public class TaskDependenciesModuleController implements ModuleController {
    private final TaskDependencies module;
    private final MainWindowController controller;

    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<ITask> taskChangeListener;

    public TaskDependenciesModuleController(IModule module, MainWindowController controller) {
        this.module = (TaskDependencies) module;
        this.controller = controller;
    }

    @Override
    public void initModule() {
        module.createPane();

        module.disableProperty().bind(controller.selectedTaskProperty().isNull());

        taskChangeListener = this::selectedTaskObserver;
        // without WeakChangeListener wrapper it can cause a memory leak, but with wrapper it doesn't work properly
        // .addListener(new WeakChangeListener<>(taskChangeListener))
        controller.selectedTaskProperty().addListener(taskChangeListener);
    }

    private void selectedTaskObserver(ObservableValue<? extends ITask> observableValue,
                                      ITask oldTask,
                                      ITask newTask) {
        if (newTask != null) {
            module.setItems(newTask.getParentTasks());
            module.getParentTaskNameColumn().setCellValueFactory(cellData -> cellData.getValue().getTask().nameProperty());
            module.getParentTaskDependencyTypeColumn().setCellValueFactory(cellData -> cellData.getValue().dependenceTypeProperty());
            // Добавляем возможность изменять тип связи в таблице
            module.setEditable(true);
            // TODO: 18.04.2016 Сделать изменение строки через команды
            module.getParentTaskDependencyTypeColumn().setCellFactory(ComboBoxTableCell.forTableColumn(TaskDependencyType.values()));
            // Контекстное меню
            ParentTaskContextMenu parentTaskContextMenu = new ParentTaskContextMenu();
            parentTaskContextMenu.initMenus(controller.getProject(), newTask);
            module.setContextMenu(parentTaskContextMenu);
        }
    }
}
