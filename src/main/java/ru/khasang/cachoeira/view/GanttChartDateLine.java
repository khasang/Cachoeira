package ru.khasang.cachoeira.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created by truesik on 08.11.2015.
 */
public class GanttChartDateLine extends HBox {
    private LocalDate startDate = LocalDate.of(2015, 11, 3);
    private LocalDate finishDate = startDate.plusWeeks(3);
    private long between;

    public GanttChartDateLine() {
        setStyle("=fx=background-color: red");
        setMaxHeight(24);
        setMinHeight(24);
        between = ChronoUnit.DAYS.between(startDate, finishDate);


        for (int i = 0; i < between; i++) {
            Label dateLabel = new Label(startDate.plusDays(i).toString());
            dateLabel.setMinWidth(70);
            dateLabel.setMinHeight(24);
            dateLabel.getStylesheets().add(this.getClass().getResource("/css/header.css").toExternalForm());
            dateLabel.setAlignment(Pos.CENTER);
//            dateLabel.minHeightProperty().bind(dateHBox.heightProperty());
            this.getChildren().add(dateLabel);
        }
    }
}
