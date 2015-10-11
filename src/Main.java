import data.IStringResource;
import data.StringResource;
import model.IProject;
import model.Project;

/**
 * Created by nadezhda on 07.10.2015.
 */

public class Main {
    public static void main(String[] args) {
        IProject pr = new Project(StringResource.getInstance().getResource("defaultProjectName"));
        System.out.println("Новый проект: " + pr.getName());
    }
}
