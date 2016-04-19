package ru.khasang.cachoeira.commands.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.khasang.cachoeira.commands.Command;
import ru.khasang.cachoeira.model.IDependentTask;
import ru.khasang.cachoeira.model.IProject;
import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

import java.util.HashMap;

public class RemoveTaskFromProjectCommand implements Command {
    private final IProject project;
    private final ITask task;
    private int index;
    private ObservableList<IDependentTask> oldParentTasks;
    private ObservableList<IDependentTask> oldChildTasks;
    private ObservableList<IResource> oldResourceList;
    private HashMap<ITask, IDependentTask> parentTaskHashMap;
    private HashMap<ITask, IDependentTask> childTaskHashMap;

    public RemoveTaskFromProjectCommand(IProject project, ITask task) {
        this.project = project;
        this.task = task;
    }

    @Override
    public void execute() {
        index = project.getTaskList().indexOf(task);
        oldParentTasks = FXCollections.observableArrayList(task.getParentTasks());
        oldChildTasks = FXCollections.observableArrayList(task.getChildTasks());
        oldResourceList = FXCollections.observableArrayList(task.getResourceList());
        parentTaskHashMap = new HashMap<>(getTaskParentDependencies(project.getTaskList()));
        childTaskHashMap = new HashMap<>(getTaskChildDependencies(project.getTaskList()));
//         Удаляем все связи с этой задачей
        task.getParentTasks().stream()
                .forEach(parentTask -> parentTask.getTask().getChildTasks()
                        .removeIf(childTask -> childTask.getTask().equals(task)));
        task.getChildTasks().stream()
                .forEach(childTask -> childTask.getTask().getParentTasks()
                        .removeIf(parentTask -> parentTask.getTask().equals(task)));
//         Вычищяем все задачи из списка предшествующих задач
        task.getParentTasks().clear();
//         Вычищаем все задачи из списка последующих задач
        task.getChildTasks().clear();
        // Вычищяем все ресурсы из списка привязанных ресурсов
        task.getResourceList().clear();
//         Удаляем эту задачу
        project.getTaskList().remove(index);
    }

    private HashMap<ITask, IDependentTask> getTaskParentDependencies(ObservableList<ITask> taskObservableList) {
        HashMap<ITask, IDependentTask> hashMap = new HashMap<>();
        for (ITask iTask : taskObservableList) {
            for (IDependentTask parentTask : iTask.getParentTasks()) {
                if (parentTask.getTask().equals(task)) {
                    hashMap.put(iTask, parentTask);
                }
            }
        }
        return hashMap;
    }

    private HashMap<ITask, IDependentTask> getTaskChildDependencies(ObservableList<ITask> taskObservableList) {
        HashMap<ITask, IDependentTask> hashMap = new HashMap<>();
        for (ITask iTask : taskObservableList) {
            for (IDependentTask childTask : iTask.getChildTasks()) {
                if (childTask.getTask().equals(task)) {
                    hashMap.put(iTask, childTask);
                }
            }
        }
        return hashMap;
    }

    @Override
    public void undo() {
        task.getResourceList().addAll(oldResourceList);
        task.getParentTasks().addAll(oldParentTasks);
        task.getChildTasks().addAll(oldChildTasks);
        project.getTaskList().add(index, task);
        // Восстанавливаем связи
        for (ITask iTask : parentTaskHashMap.keySet()) {
            iTask.getParentTasks().add(parentTaskHashMap.get(iTask));
        }
        for (ITask iTask : childTaskHashMap.keySet()) {
            iTask.getChildTasks().add(childTaskHashMap.get(iTask));
        }
    }
}
