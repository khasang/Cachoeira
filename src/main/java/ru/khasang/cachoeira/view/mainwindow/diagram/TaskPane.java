package ru.khasang.cachoeira.view.mainwindow.diagram;

import javafx.scene.control.SplitPane;
import ru.khasang.cachoeira.viewcontroller.MainWindowController;
import ru.khasang.cachoeira.view.mainwindow.diagram.buttonbox.AbstractButtonsBox;
import ru.khasang.cachoeira.view.mainwindow.diagram.ganttplan.AbstractGanttPlan;
import ru.khasang.cachoeira.view.mainwindow.diagram.tables.AbstractTableView;

public class TaskPane extends TableAndGanttPane {
    public TaskPane(MainWindowController controller, AbstractTableView tableView, AbstractGanttPlan ganttPlan, AbstractButtonsBox buttonsBox, SplitPane splitPane) {
        this.controller = controller;
        this.tableView = tableView;
        this.ganttPlan = ganttPlan;
        this.buttonBox = buttonsBox;
        this.splitPane = splitPane;
    }
}
