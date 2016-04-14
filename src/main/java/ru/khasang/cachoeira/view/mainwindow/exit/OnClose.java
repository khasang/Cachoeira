package ru.khasang.cachoeira.view.mainwindow.exit;

import javafx.stage.WindowEvent;

public interface OnClose {
    void saveProperties();

    void saveProject(WindowEvent event);
}
