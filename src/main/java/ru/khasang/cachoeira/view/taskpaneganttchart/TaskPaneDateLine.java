package ru.khasang.cachoeira.view.taskpaneganttchart;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
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
 * В этом классе описываются методы для создания шкалы с датами.
 */
public class TaskPaneDateLine extends HBox {
    private UIControl uiControl;

    public TaskPaneDateLine() {
        setAlignment(Pos.CENTER_LEFT);

        //Высота строки с датами
        setMaxHeight(24);
        setMinHeight(24);
    }

    /**
     * Метод для инициализации строки с датами
     *
     * @param projectStartDate  Начальная дата проекта
     * @param projectFinishDate Конечная дата проекта
     * @param zoomValue         Ширина одного дня в пикселях
     */
    public void initDateLine(LocalDate projectStartDate,
                             LocalDate projectFinishDate,
                             int zoomValue) {
        if (zoomValue <= 130 && zoomValue >= 101) {
            String pattern = "dd MMMM yy";
            createDateLine(projectStartDate, projectFinishDate, 1, pattern);
        } else if (zoomValue <= 100 && zoomValue >= 70) {
            String pattern = "dd.MM.yyyy";
            createDateLine(projectStartDate, projectFinishDate, 1, pattern);
        } else if (zoomValue <= 69 && zoomValue >= 55) {
            String pattern = "dd.MM.yy";
            createDateLine(projectStartDate, projectFinishDate, 1, pattern);
        } else if (zoomValue <= 54 && zoomValue >= 40) {
            String pattern1 = "dd";
            String pattern2 = "dd.MM.yy";
            createDateLine(projectStartDate, projectFinishDate, 2, pattern1, pattern2);
        } else if (zoomValue <= 39 && zoomValue >= 20) {
            String pattern1 = "dd";
            String pattern2 = "dd.MM.yy";
            createDateLine(projectStartDate, projectFinishDate, 4, pattern1, pattern2);
        } else if (zoomValue <= 19 && zoomValue >= 8) {
            String pattern = "dd.MM.yy";
            createDateLine(projectStartDate, projectFinishDate, 7, pattern);
        } else if (zoomValue <= 7 && zoomValue >= 4) {
            String pattern = "dd.MM.yy";
            createDateLine(projectStartDate, projectFinishDate, 14, pattern);
        } else if (zoomValue <= 3 && zoomValue >= 2) {
            String pattern = "dd.MM.yy";
            createDateLine(projectStartDate, projectFinishDate, 28, pattern);
        }
    }

    /**
     * Метод создающий строку с датами с заданными прамаметрами
     *
     * @param projectStartDate  Начальная дата проекта
     * @param projectFinishDate Конечная дата проекта
     * @param multiplier        Множитель
     * @param pattern           Паттерны для DateTimeFormatter (к примеру dd.MM.yyyy),
     *                          можно передать два паттерна для вида dd - dd.MM.yy.
     */
    private void createDateLine(LocalDate projectStartDate,
                                LocalDate projectFinishDate,
                                int multiplier,
                                String... pattern) {
        long between = ChronoUnit.DAYS.between(projectStartDate, projectFinishDate); //находим разницу между начальной и конечной датой проекта
        this.getChildren().clear();
        for (int i = 0; i < between; i = i + multiplier) {
            String dateFormatter;
            if (pattern.length == 1) {
                // Если передан только один паттерн, то...
                dateFormatter = DateTimeFormatter.ofPattern(pattern[0], Locale.getDefault()).format(projectStartDate.plusDays(i));
            } else {
                // ...если два, то...
                String df1 = DateTimeFormatter.ofPattern(pattern[0], Locale.getDefault()).format(projectStartDate.plusDays(i));
                String df2 = DateTimeFormatter.ofPattern(pattern[1], Locale.getDefault()).format(projectStartDate.plusDays(i + (multiplier - 1)));
                dateFormatter = df1 + " - " + df2;
            }
            Label dateLabel = createDateLabel(multiplier, dateFormatter);
            this.getChildren().add(dateLabel);
        }
    }

    /**
     * Этот метод создает лейбл с заданными параметрами
     *
     * @param multiplier    Множитель для ширины "столбца"
     * @param dateFormatter Формат даты
     * @return Лейбл
     **/
    private Label createDateLabel(int multiplier, String dateFormatter) {
        Label dateLabel = new Label(dateFormatter); //фигачим лейбл с датой на борту
        dateLabel.setTextOverrun(OverrunStyle.CLIP);
        dateLabel.setPrefWidth(uiControl.getZoomMultiplier() * multiplier);
        dateLabel.setMinHeight(24);
        dateLabel.getStylesheets().add(this.getClass().getResource("/css/header.css").toExternalForm()); //делаем красиво
        dateLabel.setAlignment(Pos.CENTER);
        return dateLabel;
    }

    /**
     * Метод в котором определяются "слушатели" на заданные параметры,
     * при изменении которых происходит перерисовка строки с датами и диаграммы.
     *
     * @param startDateProperty      Проперти начальной даты проекта
     * @param finishDateProperty     Проперти конечной даты проекта
     * @param zoomMultiplierProperty Проперти множителя
     */
    public void setListeners(ObjectProperty<LocalDate> startDateProperty,
                             ObjectProperty<LocalDate> finishDateProperty,
                             IntegerProperty zoomMultiplierProperty) {
        startDateProperty.addListener((observable -> {
            refreshDateLine(startDateProperty.getValue(), finishDateProperty.getValue(), zoomMultiplierProperty.get());
        }));
        finishDateProperty.addListener((observable -> {
            refreshDateLine(startDateProperty.getValue(), finishDateProperty.getValue(), zoomMultiplierProperty.get());
        }));
        zoomMultiplierProperty.addListener((observable -> {
            refreshDateLine(startDateProperty.getValue(), finishDateProperty.getValue(), zoomMultiplierProperty.get());
        }));
    }

    /**
     * Метод для обновления строки с датами и диаграммы
     *
     * @param projectStartDate  Начальная дата проекта
     * @param projectFinishDate Конечная дата проекта
     * @param zoomMultiplier    Множитель
     */
    private void refreshDateLine(LocalDate projectStartDate, LocalDate projectFinishDate, int zoomMultiplier) {
        initDateLine(projectStartDate, projectFinishDate, zoomMultiplier);
        uiControl.getMainWindow().getDiagramPaneController().getTaskPaneController().getTaskGanttChart().getTaskPaneObjectsLayer().refreshTaskDiagram();
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }
}
