package ru.khasang.cachoeira.data;

import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DBSchemeManager implements DataStoreInterface {
    private DBHelper dbHelper = DBHelper.getInstance();

    @Override
    public void createProjectFile(String path) {
        try {
            Statement statement = dbHelper.getConnection(path).createStatement();
            statement.executeUpdate("Some shit");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
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
        return null;
    }

    @Override
    public List<IResource> getResourceListFromFile(File file) {
        return null;
    }
}
