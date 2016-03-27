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
                        "INSERT INTO Tasks(Name, Start_Date, Finish_Date, Duration, Done_Percent, Priority_Type_Id, Cost, Description) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
                preparedStatement.setString(1, task.getName());
                preparedStatement.setString(2, task.getStartDate().toString());
                preparedStatement.setString(3, task.getFinishDate().toString());
                preparedStatement.setInt(4, task.getDuration());
                preparedStatement.setDouble(5, task.getDonePercent());
                preparedStatement.setInt(6, getTypeId(file, "Priority_Type", task.getPriorityType().toString()));
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
                        "INSERT INTO Resources(Name, Type_Id, Email, Description) " +
                        "VALUES (?, ?, ?, ?);");
                preparedStatement.setString(1, resource.getName());
                preparedStatement.setInt(2, getTypeId(file, "Resource_Type", resource.getType().toString()));
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
    public List<IDependentTask> getParentTaskListByTaskFromFile(File file, ITask task) {
        List<IDependentTask> parentTaskList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = dbHelper.getConnection(file.getPath()).prepareStatement("" +
                    "SELECT p.Task_Id AS Task_Id, d.Type AS Type " +
                    "FROM Main_Task_List_Table AS m " +
                    "JOIN Parent_Tasks AS p ON m.Parent_Task_Id = p.Id " +
                    "JOIN Dependency_Type AS d ON p.Dependency_Type_Id = d.Id " +
                    "WHERE m.Task_Id = ?;");
            preparedStatement.setInt(1, task.getId() + 1);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                IDependentTask parentTask = new DependentTask();
                for (ITask t : uiControl.getController().getProject().getTaskList()) {
                    if ((t.getId() + 1) == resultSet.getInt("Task_Id")) {
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
                    "SELECT c.Task_Id AS Task_Id, d.Type AS Type " +
                    "FROM Main_Task_List_Table AS m " +
                    "JOIN Child_Tasks AS c ON m.Child_Task_Id = c.Id " +
                    "JOIN Dependency_Type AS d ON c.Dependency_Type_Id = d.Id " +
                    "WHERE m.Task_Id = ?;");
            preparedStatement.setInt(1, task.getId() + 1);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                IDependentTask childTask = new DependentTask();
                for (ITask t : uiControl.getController().getProject().getTaskList()) {
                    if ((t.getId() + 1) == resultSet.getInt("Task_Id")) {
                        childTask.setTask(t);
                    }
                }
                childTask.setDependenceType(TaskDependencyType.valueOf(resultSet.getString("Type")));
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

    /**
     * Метод возвращающий Id типа.
     *
     * @param file  Файл проекта.
     * @param table Таблица из которой нужно получить айди типа: "Priority_Type", "Resource_Type", "Dependency_Type"
     * @param type  Тип айди которого нужно получить (в строковом виде).
     * @return Возвращает номер айди в таблице.
     */
    private int getTypeId(File file, String table, String type) {
        int resourceTypeId = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = dbHelper.getConnection(file.getPath()).prepareStatement("" +
                    "SELECT Id " +
                    "FROM " + table + " " +
                    "WHERE Type = ?;");
            preparedStatement.setString(1, type);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                resourceTypeId = resultSet.getInt("Id");
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
        return resourceTypeId;
    }
}
