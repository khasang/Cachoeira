package ru.khasang.cachoeira.view.ganttchart;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import ru.khasang.cachoeira.controller.IController;
import ru.khasang.cachoeira.view.UIControl;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * Created by truesik on 08.11.2015.
 */
public class DateLine extends HBox {
    private long between;
    private IController controller;
    private UIControl uiControl;
    private int columnWidth;

    public DateLine(IController controller, int columnWidth) {
        this.controller = controller;
        this.columnWidth = columnWidth;

        setAlignment(Pos.CENTER_LEFT);

        /** Высота полоски с датами */
        setMaxHeight(24);
        setMinHeight(24);

        controller.getProject().startDateProperty().addListener((observable, oldValue, newValue) -> {
            initDateLine();
            /** После изменения временной шкалы обновляем диаграммы */
            uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController().getTaskGanttChart().getObjectsLayer().refreshTaskDiagram();
            uiControl.getMainWindow().getDiagramPaneController().getResourcePaneController().getResourceGanttChart().getObjectsLayer().refreshResourceDiagram();
        });
        controller.getProject().finishDateProperty().addListener((observable, oldValue, newValue) -> {
            initDateLine();
            /** После изменения временной шкалы обновляем диаграммы */
            uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController().getTaskGanttChart().getObjectsLayer().refreshTaskDiagram();
            uiControl.getMainWindow().getDiagramPaneController().getResourcePaneController().getResourceGanttChart().getObjectsLayer().refreshResourceDiagram();
        });
    }

    public void initDateLine() {
        between = ChronoUnit.DAYS.between(controller.getProject().getStartDate(), controller.getProject().getFinishDate()); //находим разницу между начальной и конечной датой проекта
        this.getChildren().clear();
        for (int i = 0; i < between; i++) {
            String dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()).format(controller.getProject().getStartDate().plusDays(i));
            Label dateLabel = new Label(dateFormatter); //фигачим лейбл с датой на борту
            dateLabel.setMinWidth(columnWidth); //ширина столбца
            dateLabel.setMinHeight(24);
            dateLabel.getStylesheets().add(this.getClass().getResource("/css/header.css").toExternalForm()); //делаем красиво
            dateLabel.setAlignment(Pos.CENTER);
            this.getChildren().add(dateLabel);
        }
    }

    public void setUiControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }
}
