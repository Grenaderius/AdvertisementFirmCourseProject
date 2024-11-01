package Paths;

public class Paths {
    private final String projectPath = System.getProperty("user.dir");
    private final String imagesPath = projectPath + "\\src\\main\\resources\\Images\\";
    private final String contractsPath = projectPath + "\\src\\main\\resources\\contracts\\";
    private final String reportsPath = projectPath + "\\src\\main\\resources\\reports\\";

    public String getProjectPath() {
        return projectPath;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    public String getContractsPath() {
        return contractsPath;
    }

    public String getReportsPath(){
        return reportsPath;
    }
}
