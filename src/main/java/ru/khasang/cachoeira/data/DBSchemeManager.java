package ru.khasang.cachoeira.data;

import ru.khasang.cachoeira.model.IResource;
import ru.khasang.cachoeira.model.ITask;

import java.io.File;
import java.util.List;

/**
 * Created by Вячеслав on 21.03.2016.
 */
public class DBSchemeManager implements DataStoreInterface {
    @Override
    public void createProjectFile() {

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
