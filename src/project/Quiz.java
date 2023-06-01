package project;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Quiz {

    private String name;
    private List<Question> questions = new ArrayList<>();

    public void addQuestion(Question q) {
        this.questions.add(q);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public static Quiz loadFromFile(String path) throws FileNotFoundException {

        File file = new File(path);
        Scanner scanner = new Scanner(file);
        List<String> tmpQ = new ArrayList<>();
        Quiz quiz = new Quiz();
        while (scanner.hasNext()) {

            String temp = scanner.nextLine();
            if (temp.length() == 0) {

                if (tmpQ.size() == 2) {
                    FillIn fillIn = new FillIn();
                    fillIn.setDescription(tmpQ.get(0));
                    fillIn.setDescription(fillIn.toString());
                    fillIn.setAnswer(tmpQ.get(1));
                    quiz.addQuestion(fillIn);
                    tmpQ.clear();
                } else if (tmpQ.size() == 5) {
                    String[] options = {tmpQ.get(1), tmpQ.get(2), tmpQ.get(3), tmpQ.get(4)};
                    Test test = new Test(options, "A", "B", "C", "D");
                    test.setDescription(tmpQ.get(0));
                    test.setAnswer(tmpQ.get(1));
                    quiz.addQuestion(test);
                    tmpQ.clear();
                } else if (tmpQ.size() == 6) {
                    Special special = new Special();
                    special.setDescription(tmpQ.get(0));
                    special.setAnswer(tmpQ.get(2));
                    special.setPath(tmpQ.get(1));
                    String[] options = {tmpQ.get(2), tmpQ.get(3), tmpQ.get(4), tmpQ.get(5)};
                    special.setOptions(options);
                    quiz.addQuestion(special);
                    tmpQ.clear();
                }
            } else {
                tmpQ.add(temp);
            }
        }
        if (tmpQ.size() == 6) {
            Special special = new Special();
            special.setDescription(tmpQ.get(0));
            special.setAnswer(tmpQ.get(2));
            special.setPath(tmpQ.get(1));
            String[] options = {tmpQ.get(2), tmpQ.get(3), tmpQ.get(4), tmpQ.get(5)};
            special.setOptions(options);
            quiz.addQuestion(special);
            tmpQ.clear();
        } else if (tmpQ.size() == 2) {
            FillIn fillIn = new FillIn();
            fillIn.setDescription(tmpQ.get(0));
            fillIn.setAnswer(tmpQ.get(1));
            quiz.addQuestion(fillIn);
            tmpQ.clear();
        } else {
            String[] options = {tmpQ.get(1), tmpQ.get(2), tmpQ.get(3), tmpQ.get(4)};
            Test test = new Test(options, "A", "B", "C", "D");
            test.setDescription(tmpQ.get(0));
            test.setAnswer(tmpQ.get(1));
            quiz.addQuestion(test);
            tmpQ.clear();
        }
        return quiz;
    }
}

