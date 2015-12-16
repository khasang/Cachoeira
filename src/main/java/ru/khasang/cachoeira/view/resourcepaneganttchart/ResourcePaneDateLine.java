package ru.khasang.cachoeira.view.resourcepaneganttchart;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import ru.khasang.cachoeira.view.UIControl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * Created by truesik on 08.11.2015.
 */
public class ResourcePaneDateLine extends HBox {
    private UIControl uiControl;
    private int columnWidth;

    public ResourcePaneDateLine(int columnWidth) {
        this.columnWidth = columnWidth;

        setAlignment(Pos.CENTER_LEFT);

        /** Высота полоски с датами */
        setMaxHeight(24);
        setMinHeight(24);
    }

    public void initDateLine(LocalDate projectStartDate, LocalDate projectFinishDate) {
        long between = ChronoUnit.DAYS.between(projectStartDate, projectFinishDate);
        this.getChildren().clear();
        for (int i = 0; i < between; i++) {
            String dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()).format(projectStartDate.plusDays(i));
            Label dateLabel = new Label(dateFormatter); //фигачим лейбл с датой на борту
            dateLabel.setMinWidth(columnWidth); //ширина столбца
            dateLabel.setMinHeight(24);
            dateLabel.getStylesheets().add(this.getClass().getResource("/css/header.css").toExternalForm()); //делаем красиво
            dateLabel.setAlignment(Pos.CENTER);
            this.getChildren().add(dateLabel);
        }
    }

    public void setListeners(ObjectProperty<LocalDate> startDateProperty, ObjectProperty<LocalDate> finishDateProperty) {
        DateChangeListener dateChangeListener = new DateChangeListener();
        startDateProperty.addListener(dateChangeListener);
        finishDateProperty.addListener(dateChangeListener);
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }

    private class DateChangeListener implements ChangeListener<LocalDate> {
        @Override
        public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
            initDateLine(uiControl.getController().getProject().getStartDate(), uiControl.getController().getProject().getFinishDate());
            /** После изменения временной шкалы обновляем диаграммы */
            uiControl.getMainWindow().getDiagramPaneController().getResourcePaneController().getResourceGanttChart().getResourcePaneObjectsLayer().refreshResourceDiagram();
        }
    }
}
