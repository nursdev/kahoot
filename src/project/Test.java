package project;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test extends Question {

    private String[] options;
    private int numOfOptions;
    private List<String> labels;
    public Test(String[] options, String... labels) {
        this.options = options;
        List<String> sh = Arrays.asList(options);
        Collections.shuffle(sh);
        for(int i = 0; i < sh.size(); i++) {
            options[i] = sh.get(i);
        }
        this.labels = Arrays.asList(labels);
    }


    public String getOptionAt(int ind) {
        return options[ind];
    }

    public void setOptions(String[] options) {
        this.options = options;
    }
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            str.append(labels.get(numOfOptions++)).append(") ").append(options[i]).append(".\n");
        }
        return str.toString();
    }
}

