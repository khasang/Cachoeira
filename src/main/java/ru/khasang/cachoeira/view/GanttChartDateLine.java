package ru.khasang.cachoeira.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created by truesik on 08.11.2015.
 */
public class GanttChartDateLine extends HBox {
    private LocalDate startDate = LocalDate.of(2015, 11, 3);    //даты должны браться из controller.getProject().getStartDate()
    private LocalDate finishDate = startDate.plusWeeks(3);      //аналогично
    private long between;
    private int columnWidth;

    public GanttChartDateLine(int columnWidth) {
        this.columnWidth = columnWidth;

        setMaxHeight(24);
        setMinHeight(24);
        between = ChronoUnit.DAYS.between(startDate, finishDate); //находим разницу между начальной и конечной датой проекта
//        setAlignment(Pos.CENTER);
        setAlignment(Pos.CENTER_LEFT);


        for (int i = 0; i < between; i++) {
            Label dateLabel = new Label(startDate.plusDays(i).toString()); //фигачим лейбл с датой на борту
//            Text date = new Text(startDate.plusDays(i).toString());
//            date.setWrappingWidth(70);
//            date.setTextAlignment(TextAlignment.CENTER);
//
            dateLabel.setMinWidth(columnWidth); //ширина столбца
            dateLabel.setMinHeight(24);
            dateLabel.getStylesheets().add(this.getClass().getResource("/css/header.css").toExternalForm()); //делаем красиво
            dateLabel.setAlignment(Pos.CENTER);
            this.getChildren().add(dateLabel);
//            this.getChildren().add(date);
        }
    }
}
