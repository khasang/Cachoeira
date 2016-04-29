package ru.khasang.cachoeira.view.mainwindow.diagram;

import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.GanttPlan;
import ru.khasang.cachoeira.view.mainwindow.diagram.tables.AbstractTableView;

public class TaskPane extends TableAndGanttPane {
    public TaskPane(AbstractTableView tableView, GanttPlan ganttPlan) {
        this.tableView = tableView;
        this.ganttPlan = ganttPlan;
    }
}
