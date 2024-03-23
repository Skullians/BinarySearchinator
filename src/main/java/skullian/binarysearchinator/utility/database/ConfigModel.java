package skullian.binarysearchinator.utility.database;

public class ConfigModel {

    private String dirPath;
    private String jarType;

    public ConfigModel(String dirPath, String jarType) {
        this.dirPath = dirPath;
        this.jarType = jarType;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getJarType() {
        return jarType;
    }

    public void setJarType(String jarType) {
        this.jarType = jarType;
    }


}
