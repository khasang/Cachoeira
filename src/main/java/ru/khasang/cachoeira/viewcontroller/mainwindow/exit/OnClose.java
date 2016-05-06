package ru.khasang.cachoeira.viewcontroller.mainwindow.exit;

import javafx.stage.WindowEvent;

public interface OnClose {
    void saveProperties();

    void saveProject(WindowEvent event);
}
