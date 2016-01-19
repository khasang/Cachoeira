package ru.khasang.cachoeira.data;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by nadezhda on 07.10.2015.
 */
public class StringResourceTest extends TestCase {
    private final String idProject = "defaultProjectName";
    private final String valueProject = "Проект";
    private final String idTask = "defaultTaskName";
    private final String valueTask = "Задача";


    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetInstance() throws Exception {
        assertNotNull(StringResource.getInstance());
        assertEquals(StringResource.getInstance().getClass(), StringResource.class);
    }

    @Test
    public void testGetResource() throws Exception {
        assertEquals(StringResource.getInstance().getResource(idProject), valueProject);
        assertEquals(StringResource.getInstance().getResource(idTask), valueTask);
    }
}