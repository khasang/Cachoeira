package ru.khasang.cachoeira.data;

import ru.khasang.cachoeira.model.*;
import ru.khasang.cachoeira.view.UIControl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBSchemeManager implements DataStoreInterface {
    private DBHelper dbHelper = DBHelper.getInstance();
    private UIControl uiControl;

    public DBSchemeManager(UIControl uiControl) {
        this.uiControl = uiControl;
    }

    @Override
    public void createProjectFile(String path, IProject project) {
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        try {
            try {
                statement = dbHelper.getConnection(path).createStatement();
                String sql = new String(Files.readAllBytes(Paths.get(getClass().getResource("/sql/create.sql").toURI())), "UTF-8");
                statement.executeUpdate(sql);
                preparedStatement = dbHelper.getConnection(path).prepareStatement("INSERT INTO Project(Name, Start_Date, Finish_Date, Description) VALUES (?, ?, ?, ?);");
                preparedStatement.setString(1, project.getName());
                preparedStatement.setString(2, project.getStartDate().toString());
                preparedStatement.setString(3, project.getFinishDate().toString());
                preparedStatement.setString(4, project.getDescription());
                preparedStatement.executeUpdate();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                dbHelper.getConnection(path).close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveProjectToFile(File file) {

    }

    @Override
    public List<ITask> getTaskListFromFile(File file) {
        List<ITask> taskList = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = dbHelper.getConnection(file.getPath()).createStatement();
            resultSet = statement.executeQuery("" +
                    "SELECT Tasks.Id AS Id, Name, Start_Date, Finish_Date, Duration, Done_Percent, Cost, Description, p.Type AS Type " +
                    "FROM Tasks " +
                    "JOIN Priority_Type AS p ON Priority_Type_Id = p.Id;");
            while (resultSet.next()) {
                ITask task = new Task();
//                task.setId(resultSet.getInt("Id"));
                task.setName(resultSet.getString("Name"));
                task.setStartDate(LocalDate.parse(resultSet.getString("Start_Date")));
                task.setFinishDate(LocalDate.parse(resultSet.getString("Finish_Date")));
                task.setDuration(resultSet.getInt("Duration"));
                task.setDonePercent(resultSet.getInt("Done_Percent"));
                task.setCost(resultSet.getDouble("Cost"));
                task.setDescription(resultSet.getString("Description"));
                task.setPriorityType(PriorityType.valueOf(resultSet.getString("Type")));
                taskList.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return taskList;
    }

    @Override
    public List<IResource> getResourceListByTaskFromFile(File file, ITask task) {
        List<IResource> resourceList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = dbHelper.getConnection(file.getPath()).prepareStatement("" +
                    "SELECT Resource_Id " +
                    "FROM Main_Task_List_Table " +
                    "WHERE Task_Id = ?;");
            preparedStatement.setInt(1, task.getId() + 1);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                for (IResource resource : uiControl.getController().getProject().getResourceList()) {
                    if (resource.getId() == resultSet.getInt("Resource_Id")) {
                        resourceList.add(resource);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resourceList;
    }

    @Override
    public List<IResource> getResourceListFromFile(File file) {
        List<IResource> resourceList = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = dbHelper.getConnection(file.getPath()).createStatement();
            resultSet = statement.executeQuery("" +
                    "SELECT r.Id AS Id, r.Name AS Name, rt.Type AS Type, r.Email AS Email, r.Description AS Description " +
                    "FROM Resources AS r " +
                    "JOIN Resource_Type AS rt ON r.Type_Id = rt.Id;"
            );
            while (resultSet.next()) {
                IResource resource = new Resource();
                resource.setId(resultSet.getInt("Id"));
                resource.setName(resultSet.getString("Name"));
                resource.setType(ResourceType.valueOf(resultSet.getString("Type")));
                resource.setEmail(resultSet.getString("Email"));
                resource.setDescription(resultSet.getString("Description"));
                resourceList.add(resource);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
//                dbHelper.getConnection(file.getPath()).close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resourceList;
    }

    @Override
    public IProject getProjectFromFile(File file, IProject project) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = dbHelper.getConnection(file.getPath()).createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Project;");
            while (resultSet.next()) {
                project.setName(resultSet.getString("Name"));
                project.setStartDate(LocalDate.parse(resultSet.getString("Start_Date")));
                project.setFinishDate(LocalDate.parse(resultSet.getString("Finish_Date")));
                project.setDescription(resultSet.getString("Description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
//                dbHelper.getConnection(file.getPath()).close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return project;
    }
}
