package ru.khasang.cachoeira.properties;

import java.io.File;
import java.util.List;
import java.util.Locale;

public interface ISettingsManager {
    String getUIValueByKey(String key);

    List<File> getRecentProjects();

    String getProgramPropertyByKey(String key);

    void writeUIValues(double diagramDividerValue, double zoomValue, double width, double height, boolean isMaximized);

    void writeRecentProjects(List<File> recentProjectList);

    void writeProgramProperties(Locale locale, boolean reopenLastProject);
}
