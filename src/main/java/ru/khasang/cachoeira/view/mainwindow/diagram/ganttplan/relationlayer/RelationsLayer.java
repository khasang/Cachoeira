package ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.relationlayer;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.scene.layout.Pane;
import ru.khasang.cachoeira.model.IDependentTask;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.vcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.objectslayer.taskbar.TaskBar;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.relationlayer.relationline.RelationLine;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.relationlayer.relationline.TaskGanttPlanRelationLine;

public abstract class RelationsLayer extends Pane{
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<ITask> taskListChangeListener;

    public RelationsLayer() {
    }

    /**
     * Метод перерисовывает связи всех задач.
     *
     */
    public void refreshRelationsDiagram(MainWindowController controller) {
        this.getChildren().clear();
        for (ITask task : controller.getProject().getTaskList()) {
            for (IDependentTask dependentTask : task.getParentTasks()) {
                TaskBar parentTaskBar = controller.getTaskGanttPlan().getObjectsLayer()
                        .findTaskBarByTask(dependentTask.getTask());
                TaskBar childTaskBar = controller.getTaskGanttPlan().getObjectsLayer()
                        .findTaskBarByTask(task);
                RelationLine relationLine = new TaskGanttPlanRelationLine(
                        parentTaskBar,
                        childTaskBar,
                        dependentTask.dependenceTypeProperty()
                );
                this.getChildren().add(relationLine);
            }
        }
    }

    /**
     * Метод рисует связь между двумя задачами.
     *
     * @param parentTask Предыдущая задача (от нее идет стрелка).
     * @param childTask  Задача к которой идет стрелка.
     */
    public void addRelation(IDependentTask parentTask,
                            ITask childTask,
                            MainWindowController controller) {
        TaskBar parentTaskBar = controller.getTaskGanttPlan().getObjectsLayer()
                .findTaskBarByTask(parentTask.getTask());
        TaskBar childTaskBar = controller.getTaskGanttPlan().getObjectsLayer()
                .findTaskBarByTask(childTask);
        RelationLine relationLine = new TaskGanttPlanRelationLine(
                parentTaskBar,
                childTaskBar,
                parentTask.dependenceTypeProperty());
        this.getChildren().add(relationLine);
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
            RelationLine relationLine = (RelationLine) node;
            return relationLine.getParentTask().equals(parentTask) && relationLine.getChildTask().equals(childTask);
        });
    }

    /**
     * Метод инициализирующий листенеры.
     *
     */
    public void setListeners(MainWindowController controller) {
        // Листенер который следит за добавлением новых задач.
        // Нужен для обновления связей при изменении положения задачи в таблице задач (при драг'н'дропе).
        // Если после драг'н'дропа не обновить связь, то она работает не корректно.
        // TODO: 04.03.2016 По хорошему необходимо обновлять связь только у тех задач которые добавились, а не перерисовывать все связи.
        taskListChangeListener = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    Platform.runLater(() -> refreshRelationsDiagram(controller));
                }
            }
        };
        controller.getProject().getTaskList().addListener(new WeakListChangeListener<>(taskListChangeListener));
    }
}
