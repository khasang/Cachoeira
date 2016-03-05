package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.model.IDependentTask;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;

public class TaskPaneRelationsLayer extends Pane {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskPaneRelationsLayer.class.getName());

    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<ITask> taskListChangeListener;

    public TaskPaneRelationsLayer() {
    }

    /**
     * Метод перерисовывает связи всех задач.
     *
     * @param uiControl Контроллер представления.
     */
    public void refreshRelationsDiagram(UIControl uiControl) {
        this.getChildren().clear();
        for (ITask task : uiControl.getController().getProject().getTaskList()) {
            for (IDependentTask dependentTask : task.getParentTasks()) {
                TaskPaneTaskBar parentTaskBar = uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController()
                        .getTaskGanttChart().getTaskPaneObjectsLayer()
                        .findTaskBarByTask(dependentTask.getTask());
                TaskPaneTaskBar childTaskBar = uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController()
                        .getTaskGanttChart().getTaskPaneObjectsLayer()
                        .findTaskBarByTask(task);
                TaskPaneRelationLine relationLine = new TaskPaneRelationLine(
                        parentTaskBar,
                        childTaskBar,
                        dependentTask.dependenceTypeProperty()
                );
                this.getChildren().add(relationLine);
            }
        }
        LOGGER.debug("Диаграмма связей обновлена.");
    }

    /**
     * Метод рисует связь между двумя задачами.
     *
     * @param parentTask Предыдущая задача (от нее идет стрелка).
     * @param childTask  Задача к которой идет стрелка.
     * @param uiControl  Контроллер представления.
     */
    public void addRelation(IDependentTask parentTask,
                            ITask childTask,
                            UIControl uiControl) {
        TaskPaneTaskBar parentTaskBar = uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController()
                .getTaskGanttChart().getTaskPaneObjectsLayer()
                .findTaskBarByTask(parentTask.getTask());
        TaskPaneTaskBar childTaskBar = uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController()
                .getTaskGanttChart().getTaskPaneObjectsLayer()
                .findTaskBarByTask(childTask);
        TaskPaneRelationLine relationLine = new TaskPaneRelationLine(
                parentTaskBar,
                childTaskBar,
                parentTask.dependenceTypeProperty());
        this.getChildren().add(relationLine);
        LOGGER.debug("Связь между {} и {} добавлена на диаграмму.", parentTaskBar.getTask().getName(), childTaskBar.getTask().getName());
    }

    /**
     * Метод с помощью которого удаляется связь между задачами с диаграммы.
     *
     * @param parentTask Предыдущая задача (от нее идет стрелка).
     * @param childTask  Задача к которой идет стрелка.
     */
    public void removeRelation(ITask parentTask,
                               ITask childTask) {
        this.getChildren().removeIf(node -> {
            TaskPaneRelationLine relationLine = (TaskPaneRelationLine) node;
            return relationLine.getParentTask().equals(parentTask) && relationLine.getChildTask().equals(childTask);
        });
    }

    /**
     * Метод инициализирующий листенеры.
     *
     * @param uiControl Контроллер вью
     */
    public void setListeners(UIControl uiControl) {
        // Листенер который следит за добавлением новых задач.
        // Нужен для обновления связей при изменении положения задачи в таблице задач (при драг'н'дропе).
        // Если после драг'н'дропа не обновить связь, то она работает не корректно.
        // TODO: 04.03.2016 По хорошему необходимо обновлять связь только у тех задач которые добавились, а не перерисовывать все связи.
        taskListChangeListener = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    Platform.runLater(() -> refreshRelationsDiagram(uiControl));
                }
            }
        };
        uiControl.getController().getProject().getTaskList().addListener(new WeakListChangeListener<>(taskListChangeListener));
    }
}
