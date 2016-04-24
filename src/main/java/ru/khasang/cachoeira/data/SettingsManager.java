package ru.khasang.cachoeira.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.model.IProject;

import java.io.*;
import java.util.*;

public class SettingsManager implements ISettingsManager {
    public static final Logger LOGGER = LoggerFactory.getLogger(SettingsManager.class.getName());
    private static SettingsManager instance;

    private final File DEFAULT_DIRECTORY = new File(System.getProperty("user.home") + "/Documents/Cachoeira/settings");

    private final File UI_VALUES = new File(DEFAULT_DIRECTORY + "/ui_value.properties");
    private final File RECENT_PROJECTS = new File(DEFAULT_DIRECTORY + "/recent_projects.properties");
    private final File PROGRAM_PROPERTIES = new File(DEFAULT_DIRECTORY + "/settings.properties");

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    private SettingsManager() {
        if (DEFAULT_DIRECTORY.mkdirs()) {
            LOGGER.debug("Создана папка для хранения настроек.");
        } else {
            LOGGER.debug("Папка для хранения настроек уже существует.");
        }
        createPropertiesFileWithDefaultValues("ui");
        createPropertiesFileWithDefaultValues("recentProjects");
        createPropertiesFileWithDefaultValues("programProperties");
    }

    @Override
    public String getUIValueByKey(String key) {
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(UI_VALUES)){
            properties.load(inputStream);
            if (properties.containsKey(key)) {
                return properties.getProperty(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Can't get UI Value");
    }

    @Override
    public List<String> getRecentProjects() {
        List<String> list = new ArrayList<>();
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(RECENT_PROJECTS)){
            properties.load(inputStream);
            if (properties.containsKey("RecentProjects")) {
                String[] propertyValues = properties.getProperty("RecentProjects").split(",");
                Collections.addAll(list, propertyValues);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String getProgramPropertyByKey(String key) {
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(PROGRAM_PROPERTIES)){
            properties.load(inputStream);
            if (properties.containsKey(key)) {
                return properties.getProperty(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Can't get property");
    }

    @Override
    public void writeUIValues(double diagramDividerValue,
                              double zoomValue,
                              double width,
                              double height,
                              boolean isMaximized) {
        Properties properties = new Properties();
        try (OutputStream outputStream = new FileOutputStream(UI_VALUES)){
            properties.setProperty("DiagramDividerValue", String.valueOf(diagramDividerValue));
            properties.setProperty("ZoomValue", String.valueOf(zoomValue));
            properties.setProperty("WidthOfWindow", String.valueOf(width));
            properties.setProperty("HeightOfWindow", String.valueOf(height));
            properties.setProperty("MaximizedWindow", isMaximized ? "1" : "0");
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeRecentProjects(List<IProject> recentProjectList, IProject lastOpenedProject) {
        Properties properties = new Properties();
        StringBuilder recentProjectsValue = new StringBuilder();
        recentProjectList.forEach(recentProject -> recentProjectsValue.append(recentProject.getName()).append(","));
        recentProjectsValue.delete(recentProjectsValue.length() - 1, recentProjectsValue.length());
        try (OutputStream outputStream = new FileOutputStream(RECENT_PROJECTS)){
            properties.setProperty("RecentProjects", recentProjectsValue.toString());
            properties.setProperty("LastOpenedProject", lastOpenedProject.getName());
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeProgramProperties(Locale locale, boolean reopenLastProject) {
        Properties properties = new Properties();
        try (OutputStream outputStream = new FileOutputStream(PROGRAM_PROPERTIES)){
            properties.setProperty("Language", locale.getLanguage());
            properties.setProperty("ReopenLastProject", reopenLastProject ? "1" : "0");
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createPropertiesFileWithDefaultValues(String key) {
        switch (key) {
            case "ui":
                try {
                    if (UI_VALUES.createNewFile()) {
                        writeDefaultUIValues();
                        LOGGER.debug("Создан файл с настройками интерфейса по умолчанию");
                    } else {
                        LOGGER.debug("Файл с настройками интерфейса по умолчанию уже существует");
                    }
                } catch (IOException e) {
                    LOGGER.debug("IOException: {}", e);
                }
                break;
            case "recentProjects":
                try {
                    if (RECENT_PROJECTS.createNewFile()) {
                        writeDefaultRecentProjectValues();
                        LOGGER.debug("Создан файл для сохранения списка ранее открытых проектов");
                    } else {
                        LOGGER.debug("Файл для сохранения списка ранее открытых проектов уже существует");
                    }
                } catch (IOException e) {
                    LOGGER.debug("IOException: {}", e);
                }
                break;
            case "programProperties":
                try {
                    if (PROGRAM_PROPERTIES.createNewFile()) {
                        writeDefaultProgramValues();
                        LOGGER.debug("Создан файл с настройками программы по умолчанию");
                    } else {
                        LOGGER.debug("Файл с настройками программы по умолчанию уже существует");
                    }
                } catch (IOException e) {
                    LOGGER.debug("IOException: {}", e);
                }
        }
    }

    private void writeDefaultUIValues() {
        Properties properties = new Properties();
        try (OutputStream outputStream = new FileOutputStream(UI_VALUES)){
            properties.setProperty("DiagramDividerValue", "0.3");
            properties.setProperty("ZoomValue", "70");
            properties.setProperty("WidthOfWindow", "1280");
            properties.setProperty("HeightOfWindow", "720");
            properties.setProperty("MaximizedWindow", "0");
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeDefaultRecentProjectValues() {
        Properties properties = new Properties();
        try (OutputStream outputStream = new FileOutputStream(RECENT_PROJECTS)){
            properties.setProperty("RecentProjects", "null");
            properties.setProperty("LastOpenedProject", "null");
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeDefaultProgramValues() {
        Properties properties = new Properties();
        try (OutputStream outputStream = new FileOutputStream(PROGRAM_PROPERTIES)){
            properties.setProperty("Language", "EN");
            properties.setProperty("ReopenLastProject", "0");
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
