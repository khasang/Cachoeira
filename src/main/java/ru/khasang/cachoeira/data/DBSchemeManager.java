package ru.khasang.cachoeira.data;

import ru.khasang.cachoeira.model.*;

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
                    "SELECT *" +
                    "FROM ");
            while (resultSet.next()) {
                ITask task = new Task();
                task.setId(resultSet.getInt("Id"));
                task.setName(resultSet.getString("Name"));
                task.setStartDate(LocalDate.parse(resultSet.getString("Start_Date")));
                task.setFinishDate(LocalDate.parse(resultSet.getString("Finish_Date")));
                task.setDuration();
                task.setDonePercent();
                task.setCost();
                task.setPriorityType();
                task.setResourceList();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
