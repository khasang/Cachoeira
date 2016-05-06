package ru.khasang.cachoeira.view.mainwindow.diagram;

import ru.khasang.cachoeira.view.mainwindow.diagram.buttonbox.AbstractButtonsBox;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.AbstractGanttPlan;
import ru.khasang.cachoeira.view.mainwindow.diagram.tables.AbstractTableView;

public class ResourcePane extends TableAndGanttPane {
    public ResourcePane(AbstractTableView tableView, AbstractGanttPlan ganttPlan, AbstractButtonsBox buttonsBox) {
        this.tableView = tableView;
        this.ganttPlan = ganttPlan;
        this.buttonBox = buttonsBox;
    }
}
