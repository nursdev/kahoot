package project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class QuizMaker extends Application {
    private Stage window;
    private double WIDTH = 600;
    private double HEIGHT = 600;
    private List<Question> questions;
    private int current;
    private Timeline timeline;
    private int min;
    private int sec;
    private Label lblTimer;
    private Map<Integer, String> save = new HashMap<>();
    private boolean only = true;
    private MediaPlayer player;
    private boolean isOrderShuffled;

    @Override
    public void start(Stage primaryStage) throws Exception {
//        isOrderShuffled = true;
        window = primaryStage;
        window.getIcons().add(new Image(new FileInputStream("src/project/resources/k.png")));
        window.setScene(new Scene(selectFile(), WIDTH, HEIGHT));
        window.show();
    }

    private void music() {
        Media media = new Media(new File("src/project/resources/kahoot_music.mp3").toURI().toString());
        player = new MediaPlayer(media);
        player.play();
    }

    private StackPane selectFile() throws FileNotFoundException {
        StackPane stackPane = new StackPane();

        ImageView imageView = new ImageView(new Image(new FileInputStream("src/project/resources/welcome.gif")));
        imageView.setFitHeight(HEIGHT);
        imageView.setFitWidth(WIDTH);
        Button choose = new Button("Choose a file");
        stackPane.getChildren().addAll(imageView, choose);
        choose.setOnAction(event -> {

            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(window);
            try {
                if(isOrderShuffled) {
                    questions = Quiz.loadFromFile(file.getPath()).getQuestions();
                    for (int i = 0; i < questions.size(); i++) {
                        int switchValue = (int) (Math.random() * questions.size());
                        Question temp = questions.get(switchValue);
                        questions.set(switchValue, questions.get(i));
                        questions.set(i, temp);
                    }
                }else {
                questions = Quiz.loadFromFile(file.getPath()).getQuestions();
                }
                initialization();
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }

        });
        return stackPane;
    }

    private void initialization() throws FileNotFoundException {

        if (only) {
            timer();
            music();
            only = false;
        }
        if (questions.get(current) instanceof FillIn) {
            FillIn fillIn = (FillIn) questions.get(current);
            FillPane fillPane = new FillPane(fillIn);
            window.setScene(new Scene(fillPane, WIDTH, HEIGHT));
        } else if (questions.get(current) instanceof Test) {
            Test test = (Test) questions.get(current);
            TestPane testPane = new TestPane(test);
            window.setScene(new Scene(testPane, WIDTH, HEIGHT));
        } else if (questions.get(current) instanceof Special) {
            Special special = (Special) questions.get(current);
            SpecialPane specialPane = new SpecialPane(special);
            window.setScene(new Scene(specialPane, WIDTH, HEIGHT));
        }


    }

    private String timeFormat(int sec) {
        min = sec / 60;
        sec = sec % 60;
        String smin = min + "";
        if (min < 10) smin = "0" + smin;
        String sc = sec + "";
        if (sec < 10) sc = "0" + sc;
        return smin + ":" + sc;
    }

    public void timer() {
        timeline = new Timeline();
        lblTimer = new Label();
        lblTimer.setText(timeFormat(sec));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> lblTimer.setText(timeFormat(++sec))));
        timeline.play();
    }

    class SpecialPane extends BorderPane {

        public SpecialPane(Special special) throws FileNotFoundException {
            int order = current;
            Label description = new Label(++order + ") " + special.getDescription());
            ImageView k = new ImageView(new Image(new FileInputStream("src/project/resources/k.png")));
            k.setFitHeight(31);
            k.setFitWidth(36);
            description.setGraphic(k);
            VBox top = new VBox();
            top.getChildren().addAll(description, lblTimer);
            description.setPadding(new Insets(20, 0, 30, 0));
            top.setAlignment(Pos.CENTER);
            Button playMusic = new Button("Play");
            Button stopMusic = new Button("Stop");
            HBox h = new HBox();
            h.getChildren().addAll(playMusic, stopMusic);
            h.setAlignment(Pos.CENTER);
            setTop(top);
            Button prev = new Button("<");
            Button next = new Button(">");
            Media music = new Media(new File(special.getPath()).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(music);
            playMusic.setOnAction(event -> {
                mediaPlayer.play();
                player.stop();
            });
            stopMusic.setOnAction(event -> {
                mediaPlayer.stop();
                player.play();
            });
            setLeft(new StackPane(prev));
            setRight(new StackPane(next));
            setCenter(h);
            prev.setOnAction(event -> {
                current--;
                try {
                    initialization();
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            });
            next.setOnAction(event -> {
                current++;
                try {
                    initialization();
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }

            });
            GridPane buttons = new GridPane();
            String[] colors = {"red", "blue", "yellow", "green"};

            ToggleGroup toggleGroup = new ToggleGroup();

            RadioButton radioButton = new RadioButton(special.getOptionAt(0));
            RadioButton radioButton2 = new RadioButton(special.getOptionAt(1));
            RadioButton radioButton3 = new RadioButton(special.getOptionAt(2));
            RadioButton radioButton4 = new RadioButton(special.getOptionAt(3));

            radioButton.setMinWidth(WIDTH / 2);
            radioButton.setMinHeight(70);
            radioButton.setStyle("-fx-background-color: " + colors[0]);
            radioButton.setToggleGroup(toggleGroup);

            radioButton2.setMinWidth(WIDTH / 2);
            radioButton2.setMinHeight(70);
            radioButton2.setStyle("-fx-background-color: " + colors[1]);
            radioButton2.setToggleGroup(toggleGroup);

            radioButton3.setMinWidth(WIDTH / 2);
            radioButton3.setMinHeight(70);
            radioButton3.setStyle("-fx-background-color: " + colors[2]);
            radioButton3.setToggleGroup(toggleGroup);

            radioButton4.setMinWidth(WIDTH / 2);
            radioButton4.setMinHeight(70);
            radioButton4.setStyle("-fx-background-color: " + colors[3]);
            radioButton4.setToggleGroup(toggleGroup);

            radioButton.setOnAction(event -> {
                save.put(current, "1");
                if (radioButton.getText().equals(special.getAnswer())) special.setCheck(true);
                else special.setCheck(false);
            });
            radioButton2.setOnAction(event -> {
                save.put(current, "2");
                if (radioButton2.getText().equals(special.getAnswer())) special.setCheck(true);
                else special.setCheck(false);
            });
            radioButton3.setOnAction(event -> {
                save.put(current, "3");
                if (radioButton3.getText().equals(special.getAnswer())) special.setCheck(true);
                else special.setCheck(false);
            });
            radioButton4.setOnAction(event -> {
                save.put(current, "4");
                if (radioButton4.getText().equals(special.getAnswer())) special.setCheck(true);
                else special.setCheck(false);
            });
            if (save.containsKey(current)) {
                if (save.get(current).equals("1")) {
                    radioButton.setSelected(true);

                }
                if (save.get(current).equals("2")) {
                    radioButton2.setSelected(true);

                }
                if (save.get(current).equals("3")) {
                    radioButton3.setSelected(true);

                }
                if (save.get(current).equals("4")) {
                    radioButton4.setSelected(true);

                }
            }


            buttons.add(radioButton, 0, 0);
            buttons.add(radioButton2, 0, 1);
            buttons.add(radioButton3, 1, 0);
            buttons.add(radioButton4, 1, 1);

            buttons.setVgap(5);
            buttons.setHgap(5);
            setBottom(buttons);
            Button done = new Button("done");

            if (current == questions.size() - 1) setRight(new StackPane(done));
            done.setOnAction(event -> {
                try {
                    result();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    class TestPane extends BorderPane {

        public TestPane(Test test) throws FileNotFoundException {

            int order = current;
            Label description = new Label(++order + ") " + test.getDescription());
            ImageView k = new ImageView(new Image(new FileInputStream("src/project/resources/k.png")));
            k.setFitHeight(31);
            k.setFitWidth(36);
            description.setGraphic(k);
            VBox top = new VBox();
            top.getChildren().addAll(description, lblTimer);
            description.setPadding(new Insets(20, 0, 30, 0));
            top.setAlignment(Pos.CENTER);
            ImageView fillImage = new ImageView(new Image(new FileInputStream("src/project/resources/fillin.png")));
            fillImage.setFitWidth(450);
            fillImage.setFitHeight(400);
            setTop(top);
            Button prev = new Button("<");
            Button next = new Button(">");
            setLeft(new StackPane(prev));
            setRight(new StackPane(next));
            ImageView bgTest = new ImageView(new Image(new FileInputStream("src/project/resources/logo.png")));
            bgTest.setFitWidth(400);
            bgTest.setFitHeight(320);
            setCenter(bgTest);
            prev.setOnAction(event -> {
                current--;
                try {
                    initialization();
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            });
            next.setOnAction(event -> {
                current++;
                try {
                    initialization();
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }

            });
            GridPane buttons = new GridPane();
            String[] colors = {"red", "blue", "yellow", "green"};

            ToggleGroup toggleGroup = new ToggleGroup();

            RadioButton radioButton = new RadioButton(test.getOptionAt(0));
            RadioButton radioButton2 = new RadioButton(test.getOptionAt(1));
            RadioButton radioButton3 = new RadioButton(test.getOptionAt(2));
            RadioButton radioButton4 = new RadioButton(test.getOptionAt(3));

            radioButton.setMinWidth(WIDTH / 2);
            radioButton.setMinHeight(70);
            radioButton.setStyle("-fx-background-color: " + colors[0]);
            radioButton.setToggleGroup(toggleGroup);

            radioButton2.setMinWidth(WIDTH / 2);
            radioButton2.setMinHeight(70);
            radioButton2.setStyle("-fx-background-color: " + colors[1]);
            radioButton2.setToggleGroup(toggleGroup);

            radioButton3.setMinWidth(WIDTH / 2);
            radioButton3.setMinHeight(70);
            radioButton3.setStyle("-fx-background-color: " + colors[2]);
            radioButton3.setToggleGroup(toggleGroup);

            radioButton4.setMinWidth(WIDTH / 2);
            radioButton4.setMinHeight(70);
            radioButton4.setStyle("-fx-background-color: " + colors[3]);
            radioButton4.setToggleGroup(toggleGroup);

            radioButton.setOnAction(event -> {
                save.put(current, "1");
                if (radioButton.getText().equals(test.getAnswer())) test.setCheck(true);
                else test.setCheck(false);
            });
            radioButton2.setOnAction(event -> {
                save.put(current, "2");
                if (radioButton2.getText().equals(test.getAnswer())) test.setCheck(true);
                else test.setCheck(false);
            });
            radioButton3.setOnAction(event -> {
                save.put(current, "3");
                if (radioButton3.getText().equals(test.getAnswer())) test.setCheck(true);
                else test.setCheck(false);
            });
            radioButton4.setOnAction(event -> {
                save.put(current, "4");
                if (radioButton4.getText().equals(test.getAnswer())) test.setCheck(true);
                else test.setCheck(false);
            });

            if (save.containsKey(current)) {
                if (save.get(current).equals("1")) {
                    radioButton.setSelected(true);
                }
                if (save.get(current).equals("2")) {
                    radioButton2.setSelected(true);
                }
                if (save.get(current).equals("3")) {
                    radioButton3.setSelected(true);

                }
                if (save.get(current).equals("4")) {
                    radioButton4.setSelected(true);
                }
            }


            buttons.add(radioButton, 0, 0);
            buttons.add(radioButton2, 0, 1);
            buttons.add(radioButton3, 1, 0);
            buttons.add(radioButton4, 1, 1);

            buttons.setVgap(5);
            buttons.setHgap(5);
            setBottom(buttons);
            Button done = new Button("done");
            done.setOnAction(event -> {
                try {
                    result();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
            if (current == questions.size() - 1) setRight(new StackPane(done));
        }
    }

    class FillPane extends BorderPane {
        public FillPane(FillIn fillIn) throws FileNotFoundException {
            int order = current;
            Label description = new Label(++order + ") " + fillIn.getDescription());
            ImageView k = new ImageView(new Image(new FileInputStream("src/project/resources/k.png")));
            k.setFitHeight(31);
            k.setFitWidth(36);
            description.setGraphic(k);
            VBox top = new VBox();
            top.getChildren().addAll(description, lblTimer);
            description.setPadding(new Insets(20, 0, 30, 0));
            top.setAlignment(Pos.CENTER);
            ImageView fillImage = new ImageView(new Image(new FileInputStream("src/project/resources/triangle.gif")));
            fillImage.setFitWidth(450);
            fillImage.setFitHeight(400);
            setTop(top);
            setCenter(fillImage);
            Button prev = new Button("<");
            Button next = new Button(">");
            setLeft(new StackPane(prev));
            setRight(new StackPane(next));
            TextField fillText = new TextField();
            if (save.containsKey(current))
                fillText.setText(save.get(current).substring(4));
            fillText.setOnKeyTyped(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    String saveText = "";
                    saveText += event.getCharacter();
                    save.put(current, save.get(current) + saveText);
                    if (save.get(current).substring(4).equals(fillIn.getAnswer()) && save.containsKey(current)) {
                        fillIn.setCheck(true);
                    } else {
                        fillIn.setCheck(false);
                    }
                }
            });
            fillText.setMaxWidth(300);
            Button done = new Button("done");
            Label tYaH = new Label("Type your answer here:");
            VBox bottom = new VBox();
            bottom.setAlignment(Pos.CENTER);
            if (current == 0) prev.setVisible(false);
            if (current == questions.size() - 1) setRight(new StackPane(done));
            done.setOnAction(event -> {
                try {
                    result();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
            bottom.getChildren().addAll(tYaH, fillText);
            prev.setOnAction(event -> {
                current--;
                try {
                    initialization();
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            });
            next.setOnAction(event -> {
                current++;
                try {
                    initialization();
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }

            });
            setMargin(bottom, new Insets(0, 0, 80, 0));
            setBottom(bottom);
        }

    }

    private void result() throws FileNotFoundException {
        VBox vBox = new VBox();
        Label yourResult = new Label("Your Result");
        yourResult.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));
        yourResult.setPadding(new Insets(20, 0, 40, 0));
        int point = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).isCheck()) {
                point++;
            }
        }
        timeline.stop();
        player.stop();
        vBox.setAlignment(Pos.CENTER);
        double percent = (point * 100) / questions.size();
        String string = "";
        if (String.valueOf(percent).length() > 4) string = String.valueOf(percent).substring(0, 4);
        else string = String.valueOf(percent);
        Label percentView = new Label("" + string + "%");
        Label nOfCorAns = new Label("Number of correct answer: " + point + "/" + questions.size());
        Label finished = new Label("" + lblTimer.getText());
        Button showAnswer = new Button("Show Answer");
        Button closeTest = new Button("Close Test");
        closeTest.setOnAction(event -> {
            window.close();
        });
        showAnswer.setMinWidth(400);
        showAnswer.setMinHeight(40);
        showAnswer.setOnAction(event -> {
            try {
                current = 0;
                googleTable();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        showAnswer.setStyle("-fx-background-color: blue");
        VBox.setMargin(showAnswer, new Insets(0, 0, 20, 0));
        closeTest.setMinWidth(400);
        closeTest.setMinHeight(40);
        closeTest.setStyle("-fx-background-color: red");
        Image image = new Image(new FileInputStream("src/project/resources/RECOVER_gif_podium_2.gif"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(450);
        imageView.setFitHeight(350);
        vBox.getChildren().addAll(yourResult, percentView, nOfCorAns, finished, showAnswer, closeTest, imageView);
        window.setScene(new Scene(vBox, 600, 600));
    }

    private void googleTable() throws FileNotFoundException {

        if (questions.get(current) instanceof FillIn) {
            FillIn fillIn = (FillIn) questions.get(current);
            FillPaneCheck fillPane = new FillPaneCheck(fillIn);
            window.setScene(new Scene(fillPane, WIDTH, HEIGHT));
        } else if (questions.get(current) instanceof Test) {
            Test test = (Test) questions.get(current);
            TestPaneCheck testPane = new TestPaneCheck(test);
            window.setScene(new Scene(testPane, WIDTH, HEIGHT));
        } else if (questions.get(current) instanceof Special) {
            Special special = (Special) questions.get(current);
            SpecialPaneCheck specialPane = new SpecialPaneCheck(special);
            window.setScene(new Scene(specialPane, WIDTH, HEIGHT));
        }

    }

    class SpecialPaneCheck extends BorderPane {

        public SpecialPaneCheck(Special special) throws FileNotFoundException {

            Label desc = new Label(special.getDescription());
            setTop(new StackPane(desc));
            Button prev = new Button("<");
            Button next = new Button(">");

            setLeft(new StackPane(prev));
            setRight(new StackPane(next));
            Button button = new Button("close");
            if (current == questions.size() - 1) setRight(new StackPane(button));
            button.setOnAction((e) -> window.close());
            if (current == 0) prev.setVisible(false);
            prev.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    current--;
                    try {
                        googleTable();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            next.setOnAction(event -> {
                current++;
                try {
                    googleTable();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
            if (special.isCheck()) {
                ImageView correct = new ImageView(new Image(new FileInputStream("src/project/resources/ConstantThornyGalapagospenguin-size_restricted.gif")));
                correct.setFitWidth(300);
                correct.setFitHeight(300);
                setCenter(correct);
                Label l = new Label("Correct answer is: "+special.getAnswer());
                setBottom(new StackPane(l));
            } else {
                ImageView incorrect = new ImageView(new Image(new FileInputStream("src/project/resources/wrong.gif")));
                incorrect.setFitWidth(300);
                incorrect.setFitHeight(300);
                setCenter(incorrect);
                Label l = new Label("Correct answer is: "+special.getAnswer());
                Label y;
                if (save.containsKey(current)) {
                    y = new Label("Your output is: " + save.get(current) +" nd button");
                } else {
                    y = new Label("You don't write this question");
                }
                VBox v = new VBox();
                v.setAlignment(Pos.CENTER);
                v.getChildren().addAll(l, y);
                setBottom((v));
            }
        }

    }


    class TestPaneCheck extends BorderPane {

        public TestPaneCheck(Test test) throws FileNotFoundException {
            Label desc = new Label(test.getDescription());
            setTop(new StackPane(desc));
            Button prev = new Button("<");
            Button next = new Button(">");
            setLeft(new StackPane(prev));
            setRight(new StackPane(next));
            Button button = new Button("close");
            if (current == questions.size() - 1) setRight(new StackPane(button));
            button.setOnAction((e) -> window.close());
            if (current == 0) prev.setVisible(false);
            prev.setOnAction(event -> {
                current--;
                try {
                    googleTable();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
            next.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    current++;
                    try {
                        googleTable();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            if (test.isCheck()) {
                ImageView correct = new ImageView(new Image(new FileInputStream("src/project/resources/ConstantThornyGalapagospenguin-size_restricted.gif")));
                correct.setFitWidth(300);
                correct.setFitHeight(300);
                setCenter(correct);
                Label l = new Label(test.getAnswer());
                setBottom(new StackPane(l));

            } else {
                ImageView incorrect = new ImageView(new Image(new FileInputStream("src/project/resources/wrong.gif")));
                incorrect.setFitWidth(300);
                incorrect.setFitHeight(300);
                setCenter(incorrect);
                Label l = new Label("Correct answer is: "+test.getAnswer());
                Label y;
                if (save.containsKey(current)) {
                    y = new Label("Your output is: " + save.get(current));
                } else {
                    y = new Label("You don't write this question");
                }
                VBox v = new VBox();
                v.setAlignment(Pos.CENTER);
                v.getChildren().addAll(l, y);
                setBottom((v));
            }
        }

    }

    class FillPaneCheck extends BorderPane {

        public FillPaneCheck(FillIn fillIn) throws FileNotFoundException {
            Label desc = new Label(fillIn.getDescription());
            setTop(new StackPane(desc));
            Button prev = new Button("<");
            Button next = new Button(">");
            setLeft(new StackPane(prev));
            setRight(new StackPane(next));
            Button button = new Button("close");
            if (current == questions.size() - 1) setRight(new StackPane(button));
            button.setOnAction((e) -> window.close());
            if (current == 0) prev.setVisible(false);
            prev.setOnAction(event -> {
                current--;
                try {
                    googleTable();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
            next.setOnAction(event -> {
                current++;
                try {
                    googleTable();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });

            if (fillIn.isCheck()) {
                ImageView correct = new ImageView(new Image(new FileInputStream("src/project/resources/ConstantThornyGalapagospenguin-size_restricted.gif")));
                correct.setFitWidth(300);
                correct.setFitHeight(300);
                setCenter(correct);
                Label l = new Label(fillIn.getAnswer());
                setBottom(new StackPane(l));
            } else {
                ImageView incorrect = new ImageView(new Image(new FileInputStream("src/project/resources/wrong.gif")));
                incorrect.setFitWidth(300);
                incorrect.setFitHeight(300);
                setCenter(incorrect);
                Label l = new Label("Correct answer is: "+fillIn.getAnswer());
                Label y;
                if (save.containsKey(current)) {
                    y = new Label("Your output is: " + save.get(current).substring(4));
                } else {
                    y = new Label("You don't write this question");
                }
                VBox v = new VBox();
                v.setAlignment(Pos.CENTER);
                v.getChildren().addAll(l, y);
                setBottom((v));
            }
        }

    }


}
