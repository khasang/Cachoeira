package ru.khasang.cachoeira.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasang.cachoeira.model.IProject;

import java.io.*;
import java.util.*;

public class SettingsDAO implements ISettingsDAO {
    public static final Logger LOGGER = LoggerFactory.getLogger(SettingsDAO.class.getName());
    private static SettingsDAO instance;

    private File defaultDirectory = new File(System.getProperty("user.home") + "/Documents/Cachoeira/settings");

    private File uiValues = new File(defaultDirectory + "/ui_value.properties");
    private File recentProjects = new File(defaultDirectory + "/recent_projects.properties");
    private File programProperties = new File(defaultDirectory + "/settings.properties");

    public static SettingsDAO getInstance() {
        if (instance == null) {
            instance = new SettingsDAO();
        }
        return instance;
    }

    private SettingsDAO() {
        if (defaultDirectory.mkdirs()) {
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
        InputStream inputStream = null;
        String value = null;
        try {
            inputStream = new FileInputStream(uiValues);
            properties.load(inputStream);
            if (properties.containsKey(key)) {
                value = properties.getProperty(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeInputStream(inputStream);
        }
        return value;
    }

    @Override
    public List<String> getRecentProjects() {
        List<String> list = new ArrayList<>();
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(recentProjects);
            properties.load(inputStream);
            if (properties.containsKey("RecentProjects")) {
                String[] propertyValues = properties.getProperty("RecentProjects").split(",");
                Collections.addAll(list, propertyValues);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeInputStream(inputStream);
        }
        return list;
    }

    @Override
    public String getProgramPropertyByKey(String key) {
        Properties properties = new Properties();
        InputStream inputStream = null;
        String value = null;
        try {
            inputStream = new FileInputStream(programProperties);
            properties.load(inputStream);
            if (properties.containsKey(key)) {
                value = properties.getProperty(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeInputStream(inputStream);
        }
        return value;
    }

    @Override
    public void writeUIValues(double diagramDividerValue,
                              double zoomValue,
                              double width,
                              double height,
                              boolean isMaximized) {
        Properties properties = new Properties();
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(uiValues);
            properties.setProperty("DiagramDividerValue", String.valueOf(diagramDividerValue));
            properties.setProperty("ZoomValue", String.valueOf(zoomValue));
            properties.setProperty("WidthOfWindow", String.valueOf(width));
            properties.setProperty("HeightOfWindow", String.valueOf(height));
            properties.setProperty("MaximizedWindow", isMaximized ? "1" : "0");
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeOutputStream(outputStream);
        }
    }

    @Override
    public void writeRecentProjects(List<IProject> recentProjectList, IProject lastOpenedProject) {
        Properties properties = new Properties();
        OutputStream outputStream = null;
        StringBuilder recentProjectsValue = new StringBuilder();
        recentProjectList.forEach(recentProject -> recentProjectsValue.append(recentProject.getName()).append(","));
        recentProjectsValue.delete(recentProjectsValue.length() - 1, recentProjectsValue.length());
        try {
            outputStream = new FileOutputStream(recentProjects);
            properties.setProperty("RecentProjects", recentProjectsValue.toString());
            properties.setProperty("LastOpenedProject", lastOpenedProject.getName());
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeOutputStream(outputStream);
        }
    }

    @Override
    public void writeProgramProperties(Locale locale, boolean reopenLastProject) {
        Properties properties = new Properties();
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(programProperties);
            properties.setProperty("Language", locale.getLanguage());
            properties.setProperty("ReopenLastProject", reopenLastProject ? "1" : "0");
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeOutputStream(outputStream);
        }
    }

    private void createPropertiesFileWithDefaultValues(String key) {
        switch (key) {
            case "ui":
                try {
                    if (uiValues.createNewFile()) {
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
                    if (recentProjects.createNewFile()) {
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
                    if (programProperties.createNewFile()) {
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
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(uiValues);
            properties.setProperty("DiagramDividerValue", "0.3");
            properties.setProperty("ZoomValue", "70");
            properties.setProperty("WidthOfWindow", "1240");
            properties.setProperty("HeightOfWindow", "720");
            properties.setProperty("MaximizedWindow", "0");
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeOutputStream(outputStream);
        }
    }

    private void writeDefaultRecentProjectValues() {
        Properties properties = new Properties();
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(recentProjects);
            properties.setProperty("RecentProjects", "null");
            properties.setProperty("LastOpenedProject", "null");
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeOutputStream(outputStream);
        }
    }

    private void writeDefaultProgramValues() {
        Properties properties = new Properties();
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(programProperties);
            properties.setProperty("Language", "ENGLISH");
            properties.setProperty("ReopenLastProject", "0");
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeOutputStream(outputStream);
        }
    }

    private void closeInputStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeOutputStream(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
