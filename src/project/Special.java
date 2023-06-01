package project;

public class Special extends Question {

    private String path;
    private String[] options;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOptionAt(int ind) {
        return options[ind];
    }

    public void setOptions(String[] options) {
        this.options = options;
    }
}

