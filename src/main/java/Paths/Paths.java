package Paths;

public class Paths {
    private final String projectPath = System.getProperty("user.dir");
    private final String imagesPath = projectPath + "\\src\\main\\resources\\Images\\";
    private final String contractsPath = projectPath + "\\src\\main\\resources\\contracts\\";

    public String getProjectPath() {
        return projectPath;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    public String getContractsPath() {
        return contractsPath;
    }
}
