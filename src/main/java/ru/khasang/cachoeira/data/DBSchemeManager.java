package ru.khasang.cachoeira.data;

import org.apache.commons.io.IOUtils;
import ru.khasang.cachoeira.model.*;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBSchemeManager implements DataStoreInterface {
    private DBHelper dbHelper = DBHelper.getInstance();

    public DBSchemeManager(){
    }

    @Override
    public void createProjectFile(String path, IProject project) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dbHelper.getConnection(path);
            statement = connection.createStatement();
            String sql = new String(IOUtils.toByteArray(this.getClass().getResourceAsStream("/sql/createProject.sql")));
            statement.executeUpdate(sql);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeResources(statement);
            DBHelper.closeResources(connection);
        }
    }

    @Override
    public void createResourceExportFile(File file) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dbHelper.getConnection(file.getPath());
            statement = connection.createStatement();
            String sql = new String(IOUtils.toByteArray(this.getClass().getResourceAsStream("/sql/createResourceExport.sql")));
            statement.executeUpdate(sql);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeResources(statement);
            DBHelper.closeResources(connection);
        }
    }

    @Override
    public void saveProjectToFile(File file, IProject project) {
        deletePreviousDataFromTable(file, "Project");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbHelper.getConnection(file.getPath());
            preparedStatement = connection.prepareStatement("" +
                    "INSERT INTO Project(Name, Start_Date, Finish_Date, Description) " +
                    "VALUES (?, ?, ?, ?);");
            preparedStatement.setString(1, project.getName());
            preparedStatement.setString(2, project.getStartDate().toString());
            preparedStatement.setString(3, project.getFinishDate().toString());
            preparedStatement.setString(4, project.getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeResources(preparedStatement);
            DBHelper.closeResources(connection);
        }
    }

    @Override
    public void saveTasksToFile(File file, IProject project) {
        deletePreviousDataFromTable(file, "Tasks");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbHelper.getConnection(file.getPath());
            for (ITask task : project.getTaskList()) {
                preparedStatement = connection.prepareStatement("" +
                        "INSERT INTO Tasks(Name, Start_Date, Finish_Date, Duration, Done_Percent, Cost, Description) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?);");
                preparedStatement.setString(1, task.getName());
                preparedStatement.setString(2, task.getStartDate().toString());
                preparedStatement.setString(3, task.getFinishDate().toString());
                preparedStatement.setInt(4, task.getDuration());
                preparedStatement.setDouble(5, task.getDonePercent());
                preparedStatement.setDouble(6, task.getCost());
                preparedStatement.setString(7, task.getDescription());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeResources(preparedStatement);
            DBHelper.closeResources(connection);
        }
    }

    @Override
    public void saveResourcesToFile(File file, IProject project) {
        deletePreviousDataFromTable(file, "Resources");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbHelper.getConnection(file.getPath());
            for (IResource resource : project.getResourceList()) {
                preparedStatement = connection.prepareStatement("" +
                        "INSERT INTO Resources(Name, Resource_Type, Email, Description) " +
                        "VALUES (?, ?, ?, ?);");
                preparedStatement.setString(1, resource.getName());
                preparedStatement.setString(2, resource.getType().toString());
                preparedStatement.setString(3, resource.getEmail());
                preparedStatement.setString(4, resource.getDescription());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeResources(preparedStatement);
            DBHelper.closeResources(connection);
        }
    }

    @Override
    public void saveParentTasksToFile(File file, IProject project) {
        deletePreviousDataFromTable(file, "Parent_Tasks");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbHelper.getConnection(file.getPath());
            for (ITask task : project.getTaskList()) {
                for (IDependentTask parentTask : task.getParentTasks()) {
                    preparedStatement = connection.prepareStatement("" +
                            "INSERT INTO Parent_Tasks(Task_Id, Parent_Task_Id, Dependency_Type) " +
                            "VALUES (?, ?, ?);");
                    preparedStatement.setInt(1, getId(connection, "Tasks", task.getName()));
                    preparedStatement.setInt(2, getId(connection, "Tasks", parentTask.getTask().getName()));
                    preparedStatement.setString(3, parentTask.getDependenceType().toString());
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeResources(preparedStatement);
            DBHelper.closeResources(connection);
        }
    }

    @Override
    public void saveChildTasksToFile(File file, IProject project) {
        deletePreviousDataFromTable(file, "Child_Tasks");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbHelper.getConnection(file.getPath());
            for (ITask task : project.getTaskList()) {
                for (IDependentTask childTask : task.getChildTasks()) {
                    preparedStatement = connection.prepareStatement("" +
                            "INSERT INTO Child_Tasks(Task_Id, Child_Task_Id) " +
                            "VALUES (?, ?);");
                    preparedStatement.setInt(1, getId(connection, "Tasks", task.getName()));
                    preparedStatement.setInt(2, getId(connection, "Tasks", childTask.getTask().getName()));
//                    preparedStatement.setString(3, childTask.getDependenceType().toString());
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeResources(preparedStatement);
            DBHelper.closeResources(connection);
        }
    }

    @Override
    public void saveResourcesByTask(File file, IProject project) {
        deletePreviousDataFromTable(file, "Resources_By_Task");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            for (ITask task : project.getTaskList()) {
                for (IResource resource : task.getResourceList()) {
                    connection = dbHelper.getConnection(file.getPath());
                    preparedStatement = connection.prepareStatement("" +
                            "INSERT INTO Resources_By_Task(Task_Id, Resource_Id) " +
                            "VALUES (?, ?);");
                    preparedStatement.setInt(1, getId(connection, "Tasks", task.getName()));
                    preparedStatement.setInt(2, getId(connection, "Resources", resource.getName()));
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeResources(preparedStatement);
            DBHelper.closeResources(connection);
        }
    }

    @Override
    public List<ITask> getTaskListFromFile(File file) {
        List<ITask> taskList = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dbHelper.getConnection(file.getPath());
            statement = connection.createStatement();
            resultSet = statement.executeQuery("" +
                    "SELECT Id, Name, Start_Date, Finish_Date, Duration, Done_Percent, Cost, Description " +
                    "FROM   Tasks;");
            while (resultSet.next()) {
                ITask task = new Task();
//                task.setId(resultSet.getInt("Id"));
                task.setName(resultSet.getString("Name"));
                task.setStartDateAndVerify(LocalDate.parse(resultSet.getString("Start_Date")));
                task.setFinishDateAndVerify(LocalDate.parse(resultSet.getString("Finish_Date")));
                task.setDuration(resultSet.getInt("Duration"));
                task.setDonePercent(resultSet.getInt("Done_Percent"));
                task.setCost(resultSet.getDouble("Cost"));
                task.setDescription(resultSet.getString("Description"));
                taskList.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeResources(resultSet);
            DBHelper.closeResources(statement);
            DBHelper.closeResources(connection);
        }
        return taskList;
    }

    @Override
    public List<IResource> getResourceListByTaskFromFile(File file, IProject project, ITask task) {
        List<IResource> resourceList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dbHelper.getConnection(file.getPath());
            preparedStatement = connection.prepareStatement("" +
                    "SELECT Resource_Id " +
                    "FROM   Resources_By_Task " +
                    "WHERE  Task_Id = ?;");
            preparedStatement.setInt(1, task.getId() + 1);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                for (IResource resource : project.getResourceList()) {
                    if (resource.getId() == resultSet.getInt("Resource_Id")) {
                        resourceList.add(resource);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeResources(resultSet);
            DBHelper.closeResources(preparedStatement);
            DBHelper.closeResources(connection);
        }
        return resourceList;
    }

    @Override
    public List<IDependentTask> getParentTaskListByTaskFromFile(File file, IProject project, ITask task) {
        List<IDependentTask> parentTaskList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dbHelper.getConnection(file.getPath());
            preparedStatement = connection.prepareStatement("" +
                    "SELECT Parent_Task_Id, Dependency_Type AS Type " +
                    "FROM   Parent_Tasks " +
                    "WHERE  Task_Id = ?;");
            preparedStatement.setInt(1, task.getId() + 1);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                IDependentTask parentTask = new DependentTask();
                for (ITask t : project.getTaskList()) {
                    if ((t.getId() + 1) == resultSet.getInt("Parent_Task_Id")) {
                        parentTask.setTask(t);
                    }
                }
                parentTask.setDependenceType(TaskDependencyType.valueOf(resultSet.getString("Type")));
                parentTaskList.add(parentTask);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeResources(resultSet);
            DBHelper.closeResources(preparedStatement);
            DBHelper.closeResources(connection);
        }
        return parentTaskList;
    }

    @Override
    public List<IDependentTask> getChildTaskListByTaskFromFile(File file, IProject project, ITask task) {
        List<IDependentTask> childTaskList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dbHelper.getConnection(file.getPath());
            preparedStatement = connection.prepareStatement("" +
                    "SELECT Child_Task_Id, Dependency_Type AS Type " +
                    "FROM   Child_Tasks " +
                    "WHERE  Task_Id = ?;");
            preparedStatement.setInt(1, task.getId() + 1);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                IDependentTask childTask = new DependentTask();
                for (ITask t : project.getTaskList()) {
                    if ((t.getId() + 1) == resultSet.getInt("Child_Task_Id")) {
                        childTask.setTask(t);
                    }
                }
//                childTask.setDependenceType(TaskDependencyType.valueOf(resultSet.getString("Type")));
                childTaskList.add(childTask);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeResources(resultSet);
            DBHelper.closeResources(preparedStatement);
            DBHelper.closeResources(connection);
        }
        return childTaskList;
    }

    @Override
    public List<IResource> getResourceListFromFile(File file) {
        List<IResource> resourceList = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dbHelper.getConnection(file.getPath());
            statement = connection.createStatement();
            resultSet = statement.executeQuery("" +
                    "SELECT Id, Name, Resource_Type, Email, Description " +
                    "FROM   Resources;"
            );
            while (resultSet.next()) {
                IResource resource = new Resource();
                resource.setId(resultSet.getInt("Id"));
                resource.setName(resultSet.getString("Name"));
                resource.setType(ResourceType.valueOf(resultSet.getString("Resource_Type")));
                resource.setEmail(resultSet.getString("Email"));
                resource.setDescription(resultSet.getString("Description"));
                resourceList.add(resource);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeResources(resultSet);
            DBHelper.closeResources(statement);
            DBHelper.closeResources(connection);
        }
        return resourceList;
    }

    @Override
    public IProject getProjectFromFile(File file, IProject project) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dbHelper.getConnection(file.getPath());
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Project;");
            while (resultSet.next()) {
                project.setName(resultSet.getString("Name"));
                project.setStartDateAndVerify(LocalDate.parse(resultSet.getString("Start_Date")));
                project.setFinishDateAndVerify(LocalDate.parse(resultSet.getString("Finish_Date")));
                project.setDescription(resultSet.getString("Description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeResources(resultSet);
            DBHelper.closeResources(statement);
            DBHelper.closeResources(connection);
        }
        return project;
    }

    @Override
    public void eraseAllTables(File file) {
        deletePreviousDataFromTable(file, "Project");
        deletePreviousDataFromTable(file, "Resources_By_Task");
        deletePreviousDataFromTable(file, "Resources");
        deletePreviousDataFromTable(file, "Parent_Tasks");
        deletePreviousDataFromTable(file, "Child_Tasks");
        deletePreviousDataFromTable(file, "Tasks");
    }

    private void deletePreviousDataFromTable(File file, String table) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dbHelper.getConnection(file.getPath());
            statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM " + table + ";");
            statement.executeUpdate("UPDATE sqlite_sequence SET seq = 0 WHERE name = '" + table + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeResources(statement);
            DBHelper.closeResources(connection);
        }
    }

    private int getId(Connection connection, String table, String name) {
        int id = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT Id " +
                    "FROM " + table + " " +
                    "WHERE  Name = ?;");
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("Id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeResources(resultSet);
            DBHelper.closeResources(preparedStatement);
        }
        return id;
    }
}
