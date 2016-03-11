package ru.khasang.cachoeira.data;

import ru.khasang.cachoeira.model.IProject;

import java.util.List;
import java.util.Locale;

public interface ISettingsDAO {
    String getUIValueByKey(String key);

    List<String> getRecentProjects();

    String getProgramPropertyByKey(String key);

    void writeUIValues(double diagramDividerValue, double zoomValue, double width, double height, boolean isMaximized);

    void writeRecentProjects(List<IProject> recentProjectList, IProject lastOpenedProject);

    void writeProgramProperties(Locale locale, boolean reopenLastProject);
}
