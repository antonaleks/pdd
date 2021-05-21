package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXMasonryPane;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.antonaleks.pdd.entity.Option;
import org.antonaleks.pdd.entity.Question;
import org.antonaleks.pdd.entity.Session;
import org.antonaleks.pdd.model.Exam;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

//@ViewController(value = "/fxml/Main.fxml", title = "PDD")
public class ExamController extends BaseController {
    @FXML
    public JFXListView topicsListView;
    @FXML
    public JFXMasonryPane masonryPane;
    @FXML
    public Label textQuestion;
    public ImageView imageView;
    public StackPane buttonStackPane;

    public Label timerLabel;
    public JFXButton alertButton;
    public JFXButton closeButton;
    private List<JFXButton> buttons;
    private int currentQuestion;
    private Exam exam;

    @FXML
    public void initialize() throws IOException {
        exam = new Exam(Session.getInstance().getCurrentCategory());
        setInitProps(exam);
    }

    public void setInitProps(Exam exam) throws IOException {

        ArrayList<Node> children = new ArrayList<>();
        int i = 1;
        buttons = new ArrayList<JFXButton>();
        for (Question question :
                exam.getTicket().getQuestions()) {
            JFXButton button = new JFXButton();
            button.setStyle("-fx-text-fill:WHITE;-fx-background-color:#5264AE;");
            button.setText("" + i++);
            button.setOnMouseClicked(e -> {
                currentQuestion = Integer.parseInt(button.getText());
                setQuestionForButton(question, button);
            });
            buttons.add(button);
        }

        children.addAll(buttons);
        masonryPane.getChildren().addAll(children);

        setQuestionForButton(exam.getTicket().getQuestions().get(0), buttons.get(0));

        AtomicInteger time = new AtomicInteger();
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        ae -> {
                            time.getAndIncrement();
                            int seconds = time.get() % 60;
                            int minutes = (time.get() % 3600) / 60;

                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.MINUTE, minutes);
                            cal.set(Calendar.SECOND, seconds);

                            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");

                            timerLabel.setText(sdf.format(cal.getTime()));

                        }
                )
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        currentQuestion = 1;


    }

    private void setQuestionForButton(Question question, JFXButton button) {

        textQuestion.setText(question.getText());

        ObservableList<Object> observableListQuestion = FXCollections.observableArrayList();
        Collections.shuffle(question.getOptions());
        observableListQuestion.setAll(question.getOptions());
        if (!button.getStyle().contains("Green") && !button.getStyle().contains("Red"))
            button.setStyle("-fx-background-color:#8d9bd7;");

        topicsListView.setItems(observableListQuestion);


        topicsListView.setCellFactory(i -> new ListCell<Option>() {
            private boolean correct = false;
            private String defaultStyle = getStyle();

            @Override
            public void updateItem(Option item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    int id = getIndex() + 1;
                    Text text = new Text("" + id + ". " + item);
                    text.setWrappingWidth(700);
                    correct = question.getRightOption() == item.getId();
                    setGraphic(text);
                    if (item.isChecked())
                        setStyle("-fx-background-color: Blue;");
                }
            }

            @Override
            public void updateSelected(boolean selected) {
                super.updateSelected(selected);
                if (selected) {
//                    setStyle("-fx-background-color: Blue;");
                    getItem().setChecked();
                    //nextQuestion();
                } else {
                    setStyle(defaultStyle);
                    getItem().setUnchecked();
                }
            }

        });

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(question.getImage().getBytes()));
            Image img = new Image(bis);
            imageView.setImage(img);
        } catch (Exception ex) {
            imageView.setImage(null);
        }

    }

    @FXML
    public void terminateTraining(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void start(Stage window, int number) throws Exception {


    }

    public void nextQuestion() {
        currentQuestion++;
        if (currentQuestion <= exam.getTicket().getQuestions().size())
            setQuestionForButton(exam.getTicket().getQuestions().get(currentQuestion - 1), buttons.get(currentQuestion - 1));

    }
}
