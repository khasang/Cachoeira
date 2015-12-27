package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.HBox;
import ru.khasang.cachoeira.view.UIControl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * Created by truesik on 08.11.2015.
 */
public class TaskPaneDateLine extends HBox {
    private UIControl uiControl;

    public TaskPaneDateLine() {
        setAlignment(Pos.CENTER_LEFT);

        /** Высота полоски с датами */
        setMaxHeight(24);
        setMinHeight(24);
    }

    public void initDateLine(LocalDate projectStartDate,
                             LocalDate projectFinishDate,
                             int zoomValue) {
        if (zoomValue <= 130 && zoomValue >= 101) {
            long between = ChronoUnit.DAYS.between(projectStartDate, projectFinishDate); //находим разницу между начальной и конечной датой проекта
            this.getChildren().clear();
            for (int i = 0; i < between; i++) {
                String dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault()).format(projectStartDate.plusDays(i));
                Label dateLabel = new Label(dateFormatter); //фигачим лейбл с датой на борту
                dateLabel.setTextOverrun(OverrunStyle.CLIP);
                dateLabel.setPrefWidth(uiControl.getZoomMultiplier());
                dateLabel.setMinHeight(24);
                dateLabel.getStylesheets().add(this.getClass().getResource("/css/header.css").toExternalForm()); //делаем красиво
                dateLabel.setAlignment(Pos.CENTER);
                this.getChildren().add(dateLabel);
            }
        }
        if (zoomValue <= 100 && zoomValue >= 70) {
            long between = ChronoUnit.DAYS.between(projectStartDate, projectFinishDate); //находим разницу между начальной и конечной датой проекта
            this.getChildren().clear();
            for (int i = 0; i < between; i++) {
                String dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()).format(projectStartDate.plusDays(i));
                Label dateLabel = new Label(dateFormatter); //фигачим лейбл с датой на борту
                dateLabel.setTextOverrun(OverrunStyle.CLIP);
                dateLabel.setPrefWidth(uiControl.getZoomMultiplier());
                dateLabel.setMinHeight(24);
                dateLabel.getStylesheets().add(this.getClass().getResource("/css/header.css").toExternalForm()); //делаем красиво
                dateLabel.setAlignment(Pos.CENTER);
                this.getChildren().add(dateLabel);
            }
        }
        if (zoomValue <= 69 && zoomValue >= 55) {
            long between = ChronoUnit.DAYS.between(projectStartDate, projectFinishDate); //находим разницу между начальной и конечной датой проекта
            this.getChildren().clear();
            for (int i = 0; i < between; i++) {
                String dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy", Locale.getDefault()).format(projectStartDate.plusDays(i));
                Label dateLabel = new Label(dateFormatter); //фигачим лейбл с датой на борту
                dateLabel.setTextOverrun(OverrunStyle.CLIP);
                dateLabel.setPrefWidth(uiControl.getZoomMultiplier());
                dateLabel.setMinHeight(24);
                dateLabel.getStylesheets().add(this.getClass().getResource("/css/header.css").toExternalForm()); //делаем красиво
                dateLabel.setAlignment(Pos.CENTER);
                this.getChildren().add(dateLabel);
            }
        }
        if (zoomValue <= 54 && zoomValue >= 40) {
            long between = ChronoUnit.DAYS.between(projectStartDate, projectFinishDate); //находим разницу между начальной и конечной датой проекта
            this.getChildren().clear();
            for (int i = 0; i < between; i = i + 2) {
                String dd = DateTimeFormatter.ofPattern("dd", Locale.getDefault()).format(projectStartDate.plusDays(i));
                String dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy", Locale.getDefault()).format(projectStartDate.plusDays(i + 1));
                Label dateLabel = new Label(dd + " - " + dateFormatter); //фигачим лейбл с датой на борту
                dateLabel.setTextOverrun(OverrunStyle.CLIP);
                dateLabel.setPrefWidth(uiControl.getZoomMultiplier() * 2);
                dateLabel.setMinHeight(24);
                dateLabel.getStylesheets().add(this.getClass().getResource("/css/header.css").toExternalForm()); //делаем красиво
                dateLabel.setAlignment(Pos.CENTER);
                this.getChildren().add(dateLabel);
            }
        }
        if (zoomValue <= 39 && zoomValue >= 20) {
            long between = ChronoUnit.DAYS.between(projectStartDate, projectFinishDate); //находим разницу между начальной и конечной датой проекта
            this.getChildren().clear();
            for (int i = 0; i < between; i = i + 4) {
                String dd = DateTimeFormatter.ofPattern("dd", Locale.getDefault()).format(projectStartDate.plusDays(i));
                String dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy", Locale.getDefault()).format(projectStartDate.plusDays(i + 3));
                Label dateLabel = new Label(dd + " - " + dateFormatter); //фигачим лейбл с датой на борту
                dateLabel.setTextOverrun(OverrunStyle.CLIP);
                dateLabel.setPrefWidth(uiControl.getZoomMultiplier() * 4);
                dateLabel.setMinHeight(24);
                dateLabel.getStylesheets().add(this.getClass().getResource("/css/header.css").toExternalForm()); //делаем красиво
                dateLabel.setAlignment(Pos.CENTER);
                this.getChildren().add(dateLabel);
            }
        }
        if (zoomValue <= 19 && zoomValue >= 8) {
            long between = ChronoUnit.DAYS.between(projectStartDate, projectFinishDate); //находим разницу между начальной и конечной датой проекта
            this.getChildren().clear();
            for (int i = 0; i < between; i = i + 7) {
//                String dd = DateTimeFormatter.ofPattern("dd", Locale.getDefault()).format(projectStartDate.plusDays(i));
                String dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy", Locale.getDefault()).format(projectStartDate.plusDays(i));
                Label dateLabel = new Label(dateFormatter); //фигачим лейбл с датой на борту
                dateLabel.setTextOverrun(OverrunStyle.CLIP);
                dateLabel.setPrefWidth(uiControl.getZoomMultiplier() * 7);
                dateLabel.setMinHeight(24);
                dateLabel.getStylesheets().add(this.getClass().getResource("/css/header.css").toExternalForm()); //делаем красиво
                dateLabel.setAlignment(Pos.CENTER);
                this.getChildren().add(dateLabel);
            }
        }
        if (zoomValue <= 7 && zoomValue >= 4) {
            long between = ChronoUnit.DAYS.between(projectStartDate, projectFinishDate); //находим разницу между начальной и конечной датой проекта
            this.getChildren().clear();
            for (int i = 0; i < between; i = i + 14) {
//                String dd = DateTimeFormatter.ofPattern("dd", Locale.getDefault()).format(projectStartDate.plusDays(i));
                String dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy", Locale.getDefault()).format(projectStartDate.plusDays(i));
                Label dateLabel = new Label(dateFormatter); //фигачим лейбл с датой на борту
                dateLabel.setTextOverrun(OverrunStyle.CLIP);
                dateLabel.setPrefWidth(uiControl.getZoomMultiplier() * 14);
                dateLabel.setMinHeight(24);
                dateLabel.getStylesheets().add(this.getClass().getResource("/css/header.css").toExternalForm()); //делаем красиво
                dateLabel.setAlignment(Pos.CENTER);
                this.getChildren().add(dateLabel);
            }
        }
        if (zoomValue <= 3 && zoomValue >= 2) {
            long between = ChronoUnit.DAYS.between(projectStartDate, projectFinishDate); //находим разницу между начальной и конечной датой проекта
            this.getChildren().clear();
            for (int i = 0; i < between; i = i + 28) {
//                String dd = DateTimeFormatter.ofPattern("dd", Locale.getDefault()).format(projectStartDate.plusDays(i));
                String dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy", Locale.getDefault()).format(projectStartDate.plusDays(i));
                Label dateLabel = new Label(dateFormatter); //фигачим лейбл с датой на борту
                dateLabel.setTextOverrun(OverrunStyle.CLIP);
                dateLabel.setPrefWidth(uiControl.getZoomMultiplier() * 28);
                dateLabel.setMinHeight(24);
                dateLabel.getStylesheets().add(this.getClass().getResource("/css/header.css").toExternalForm()); //делаем красиво
                dateLabel.setAlignment(Pos.CENTER);
                this.getChildren().add(dateLabel);
            }
        }
    }

    public void setListeners(ObjectProperty<LocalDate> startDateProperty,
                             ObjectProperty<LocalDate> finishDateProperty) {
        DateChangeListener dateChangeListener = new DateChangeListener();
        startDateProperty.addListener(dateChangeListener);
        finishDateProperty.addListener(dateChangeListener);
        uiControl.zoomMultiplierProperty().addListener((observable -> {
            initDateLine(uiControl.getController().getProject().getStartDate(), uiControl.getController().getProject().getFinishDate(), uiControl.getZoomMultiplier());
            uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController().getTaskGanttChart().getTaskPaneObjectsLayer().refreshTaskDiagram();
        }));
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }

    private class DateChangeListener implements ChangeListener<LocalDate> {
        @Override
        public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
            initDateLine(uiControl.getController().getProject().getStartDate(), uiControl.getController().getProject().getFinishDate(), uiControl.getZoomMultiplier());
            /** После изменения временной шкалы обновляем диаграммы */
            uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController().getTaskGanttChart().getTaskPaneObjectsLayer().refreshTaskDiagram();
        }
    }
}
