package project;


public class FillIn extends Question {

    public String toString() {
        return super.getDescription().replace("{blank}", "_____");
    }

}

