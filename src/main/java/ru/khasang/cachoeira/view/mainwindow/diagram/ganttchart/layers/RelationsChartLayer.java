package ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.layers;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.scene.layout.Pane;
import ru.khasang.cachoeira.model.IDependentTask;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.relationline.RelationLine;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.relationline.TaskGanttChartRelationLine;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttchart.taskbar.TaskBar;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;

/**
 * Layer that views relations between tasks.
 */
public class RelationsChartLayer extends Pane implements AbstractChartLayer {
    private MainWindowController controller;

    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<ITask> taskListChangeListener;

    /**
     * @param controller MainWindowController.
     */
    public RelationsChartLayer(MainWindowController controller) {
        this.controller = controller;
    }

    @Override
    public void createLayer() {

    }

    /**
     * Метод перерисовывает связи всех задач.
     */
    public void refreshRelationsDiagram() {
        this.getChildren().clear();
        ObjectsChartLayer objectsChartLayer = (ObjectsChartLayer) controller.getTaskGanttChart().getObjectsChartLayer();
        for (ITask task : controller.getProject().getTaskList()) {
            for (IDependentTask dependentTask : task.getParentTasks()) {
                TaskBar parentTaskBar = objectsChartLayer.findTaskBarByTask(dependentTask.getTask());
                TaskBar childTaskBar = objectsChartLayer.findTaskBarByTask(task);
                RelationLine relationLine = new TaskGanttChartRelationLine(
                        parentTaskBar,
                        childTaskBar,
                        dependentTask.dependenceTypeProperty()
                );
                this.getChildren().add(relationLine);
            }
        }
    }

    /**
     * Draws relation between two task bars.
     *
     * @param parentTask Предыдущая задача (от нее идет стрелка).
     * @param childTask  Задача к которой идет стрелка.
     */
    public void addRelation(IDependentTask parentTask,
                            ITask childTask) {
        ObjectsChartLayer objectsChartLayer = (ObjectsChartLayer) controller.getTaskGanttChart().getObjectsChartLayer();
        TaskBar parentTaskBar = objectsChartLayer.findTaskBarByTask(parentTask.getTask());
        TaskBar childTaskBar = objectsChartLayer.findTaskBarByTask(childTask);
        RelationLine relationLine = new TaskGanttChartRelationLine(
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
     */
    public void setListeners() {
        // Листенер который следит за добавлением новых задач.
        // Нужен для обновления связей при изменении положения задачи в таблице задач (при драг'н'дропе).
        // Если после драг'н'дропа не обновить связь, то она работает не корректно.
        // TODO: 04.03.2016 По хорошему необходимо обновлять связь только у тех задач которые добавились, а не перерисовывать все связи.
        taskListChangeListener = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    Platform.runLater(this::refreshRelationsDiagram);
                }
            }
        };
        controller.getProject().getTaskList().addListener(new WeakListChangeListener<>(taskListChangeListener));
    }
}
