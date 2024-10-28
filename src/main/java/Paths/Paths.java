package Paths;

public class Paths {
    private final String projectPath = System.getProperty("user.dir");
    private final String imagesPath = projectPath + "\\Images\\";

    public String getProjectPath() {
        return projectPath;
    }

    public String getImagesPath() {
        return imagesPath;
    }
}
