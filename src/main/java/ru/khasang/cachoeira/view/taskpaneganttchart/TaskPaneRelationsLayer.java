package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.model.IDependentTask;
import ru.khasang.cachoeira.model.ITask;
import ru.khasang.cachoeira.view.UIControl;

public class TaskPaneRelationsLayer extends Pane {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskPaneRelationsLayer.class.getName());

    @SuppressWarnings("FieldCanBeLocal")
    private InvalidationListener zoomMultiplierListener;

    public TaskPaneRelationsLayer() {
    }

    /**
     *
     * @param uiControl
     */
    @Deprecated
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
     *
     * @param dependentTask
     * @param task
     * @param uiControl
     */
    public void addRelation(IDependentTask dependentTask,
                            ITask task,
                            UIControl uiControl) {
        TaskPaneTaskBar parentTaskBar = uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController()
                .getTaskGanttChart().getTaskPaneObjectsLayer()
                .findTaskBarByTask(dependentTask.getTask());
        TaskPaneTaskBar childTaskBar = uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController()
                .getTaskGanttChart().getTaskPaneObjectsLayer()
                .findTaskBarByTask(task);
        TaskPaneRelationLine relationLine = new TaskPaneRelationLine(
                parentTaskBar,
                childTaskBar,
                dependentTask.dependenceTypeProperty());
        this.getChildren().add(relationLine);
        LOGGER.debug("Связь между {} и {} добавлена на диаграмму.", parentTaskBar.getTask().getName(), childTaskBar.getTask().getName());
    }

    /**
     *
     * @param parentTask
     * @param childTask
     */
    public void removeRelation(ITask parentTask,
                               ITask childTask) {
        this.getChildren().removeIf(node -> {
            TaskPaneRelationLine relationLine = (TaskPaneRelationLine) node;
            return relationLine.getParentTask().equals(parentTask) && relationLine.getChildTask().equals(childTask);
        });
    }

    /**
     * Если переменная зума меняется то обновляем диаграмму
     *
     * @param uiControl Контроллер вью
     */
    public void setListeners(UIControl uiControl) {
        zoomMultiplierListener = observable -> refreshRelationsDiagram(uiControl);
        uiControl.zoomMultiplierProperty().addListener(new WeakInvalidationListener(zoomMultiplierListener));
    }
}
