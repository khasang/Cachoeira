package ru.khasang.cachoeira.view.model;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.khasang.cachoeira.model.Project;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by nadezhda on 07.10.2015.
 */
public class ProjectTest extends TestCase {

    private Project project;
    private final String name = "Project";
    private final String newName = "New project";
    private final Date start = new Date();
    private final Date finish = new Date();

    @Before
    public void setUp() throws Exception {
        project = new Project(name);
        Calendar.getInstance().set(2015, 10, 7);
        start.setTime(Calendar.getInstance().getTimeInMillis());
        Calendar.getInstance().set(2015, 12, 30);
        finish.setTime(Calendar.getInstance().getTimeInMillis());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSave() throws Exception {

    }

    @Test
    public void testLoad() throws Exception {

    }

    @Test
    public void testGetName() throws Exception {
        assertEquals(project.getName(), name);
    }

    @Test
    public void testSetName() throws Exception {
        project.setName(newName);
        assertEquals(project.getName(), newName);
    }

    @Test
    public void testGetStart() throws Exception {
        project.setStart(start);
        assertEquals(project.getStart(), start);
    }

    @Test
    public void testSetStart() throws Exception {
        project.setStart(start);
        assertEquals(project.getStart(), start);
    }

    @Test
    public void testGetFinish() throws Exception {
        project.setFinish(finish);
        assertEquals(project.getFinish(), finish);
    }

    @Test
    public void testSetFinish() throws Exception {
        project.setFinish(finish);
        assertEquals(project.getFinish(), finish);
    }

    @Test
    public void testGetTaskList() throws Exception {

    }

    @Test
    public void testSetTaskList() throws Exception {

    }
}