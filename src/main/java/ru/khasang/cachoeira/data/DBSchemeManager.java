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
        try {
            try {
                statement = dbHelper.getConnection(path).createStatement();
                String sql = new String(Files.readAllBytes(Paths.get(getClass().getResource("/sql/create.sql").toURI())), "UTF-8");
                statement.executeUpdate(sql);
                uiControl.setFile(new File(path));
                saveProjectToFile(uiControl.getFile(), project);
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
//                dbHelper.getConnection(path).close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveProjectToFile(File file, IProject project) {
        deletePreviousDataFromTable(file, "Project");
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = dbHelper.getConnection(file.getPath()).prepareStatement("" +
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
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveTasksToFile(File file, IProject project) {
        deletePreviousDataFromTable(file, "Tasks");
        PreparedStatement preparedStatement = null;
        try {
            for (ITask task : project.getTaskList()) {
                preparedStatement = dbHelper.getConnection(file.getPath()).prepareStatement("" +
                        "INSERT INTO Tasks(Name, Start_Date, Finish_Date, Duration, Done_Percent, Priority_Type, Cost, Description) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
                preparedStatement.setString(1, task.getName());
                preparedStatement.setString(2, task.getStartDate().toString());
                preparedStatement.setString(3, task.getFinishDate().toString());
                preparedStatement.setInt(4, task.getDuration());
                preparedStatement.setDouble(5, task.getDonePercent());
                preparedStatement.setString(6, task.getPriorityType().toString());
                preparedStatement.setDouble(7, task.getCost());
                preparedStatement.setString(8, task.getDescription());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveResourcesToFile(File file, IProject project) {
        deletePreviousDataFromTable(file, "Resources");
        PreparedStatement preparedStatement = null;
        try {
            for (IResource resource : project.getResourceList()) {
                preparedStatement = dbHelper.getConnection(file.getPath()).prepareStatement("" +
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
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveParentTasksToFile(File file, IProject project) {
        deletePreviousDataFromTable(file, "Parent_Tasks");
        PreparedStatement preparedStatement = null;
        try {
            for (ITask task : uiControl.getController().getProject().getTaskList()) {
                for (IDependentTask parentTask : task.getParentTasks()) {
                    preparedStatement = dbHelper.getConnection(file.getPath()).prepareStatement("" +
                            "INSERT INTO Parent_Tasks(Task_Id, Parent_Task_Id, Dependency_Type) " +
                            "VALUES (?, ?, ?);");
                    preparedStatement.setInt(1, getId(file, "Tasks", task.getName()));
                    preparedStatement.setInt(2, getId(file, "Tasks", parentTask.getTask().getName()));
                    preparedStatement.setString(3, parentTask.getDependenceType().toString());
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveChildTasksToFile(File file, IProject project) {
        deletePreviousDataFromTable(file, "Child_Tasks");
        PreparedStatement preparedStatement = null;
        try {
            for (ITask task : uiControl.getController().getProject().getTaskList()) {
                for (IDependentTask childTask : task.getChildTasks()) {
                    preparedStatement = dbHelper.getConnection(file.getPath()).prepareStatement("" +
                            "INSERT INTO Child_Tasks(Task_Id, Child_Task_Id) " +
                            "VALUES (?, ?);");
                    preparedStatement.setInt(1, getId(file, "Tasks", task.getName()));
                    preparedStatement.setInt(2, getId(file, "Tasks", childTask.getTask().getName()));
//                    preparedStatement.setString(3, childTask.getDependenceType().toString());
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveResourcesByTask(File file, IProject project) {
        deletePreviousDataFromTable(file, "Resources_By_Task");
        PreparedStatement preparedStatement;
        try {
            for (ITask task : uiControl.getController().getProject().getTaskList()) {
                for (IResource resource : task.getResourceList()) {
                    preparedStatement = dbHelper.getConnection(file.getPath()).prepareStatement("" +
                            "INSERT INTO Resources_By_Task(Task_Id, Resource_Id) " +
                            "VALUES (?, ?);");
                    preparedStatement.setInt(1, getId(file, "Tasks", task.getName()));
                    preparedStatement.setInt(2, getId(file, "Resources", resource.getName()));
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ITask> getTaskListFromFile(File file) {
        List<ITask> taskList = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = dbHelper.getConnection(file.getPath()).createStatement();
            resultSet = statement.executeQuery("" +
                    "SELECT Id, Name, Start_Date, Finish_Date, Duration, Done_Percent, Cost, Description, Priority_Type " +
                    "FROM   Tasks;");
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
                task.setPriorityType(PriorityType.valueOf(resultSet.getString("Priority_Type")));
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
                    "FROM   Resources_By_Task " +
                    "WHERE  Task_Id = ?;");
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
    public List<IDependentTask> getParentTaskListByTaskFromFile(File file, ITask task) {
        List<IDependentTask> parentTaskList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = dbHelper.getConnection(file.getPath()).prepareStatement("" +
                    "SELECT Parent_Task_Id, Dependency_Type AS Type " +
                    "FROM   Parent_Tasks " +
                    "WHERE  Task_Id = ?;");
            preparedStatement.setInt(1, task.getId() + 1);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                IDependentTask parentTask = new DependentTask();
                for (ITask t : uiControl.getController().getProject().getTaskList()) {
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
        return parentTaskList;
    }

    @Override
    public List<IDependentTask> getChildTaskListByTaskFromFile(File file, ITask task) {
        List<IDependentTask> childTaskList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = dbHelper.getConnection(file.getPath()).prepareStatement("" +
                    "SELECT Child_Task_Id, Dependency_Type AS Type " +
                    "FROM   Child_Tasks " +
                    "WHERE  Task_Id = ?;");
            preparedStatement.setInt(1, task.getId() + 1);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                IDependentTask childTask = new DependentTask();
                for (ITask t : uiControl.getController().getProject().getTaskList()) {
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
        return childTaskList;
    }

    @Override
    public List<IResource> getResourceListFromFile(File file) {
        List<IResource> resourceList = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = dbHelper.getConnection(file.getPath()).createStatement();
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

    private void deletePreviousDataFromTable(File file, String table) {
        Statement statement = null;
        try {
            statement = dbHelper.getConnection(file.getPath()).createStatement();
            statement.executeUpdate("DELETE FROM " + table + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private int getId(File file, String table, String name) {
        int id = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = dbHelper.getConnection(file.getPath()).prepareStatement("" +
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
        return id;
    }
}
