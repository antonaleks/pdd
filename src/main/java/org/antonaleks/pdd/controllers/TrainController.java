package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.*;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.antonaleks.pdd.entity.Option;
import org.antonaleks.pdd.entity.Question;
import org.antonaleks.pdd.entity.Session;
import org.antonaleks.pdd.entity.Topic;
import org.antonaleks.pdd.model.Training;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

//@ViewController(value = "/fxml/Main.fxml", title = "PDD")
public class TrainController extends BaseController {
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
    public Label textTip;
    private String currentTip;
    private List<JFXButton> buttons;
    private int currentQuestion;
    private Training training;

    @FXML
    public void initialize(int numberTicket) throws IOException {
        training = new Training(numberTicket, Session.getInstance().getCurrentCategory());
        setInitProps(training);
    }

    @FXML
    public void initialize(Topic topic) throws IOException {
        training = new Training(topic, Session.getInstance().getCurrentCategory());
        setInitProps(training);
    }


    public void setInitProps(Training training) throws IOException {

        alertButton.setOnAction(action -> {
            JFXAlert alert = new JFXAlert((Stage) alertButton.getScene().getWindow());
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setOverlayClose(false);
            JFXDialogLayout layout = new JFXDialogLayout();
            layout.setHeading(new Label("Подсказка"));
            layout.setBody(new Label(currentTip));
            JFXButton closeButton = new JFXButton("Ок");
            closeButton.getStyleClass().add("dialog-accept");
            closeButton.setOnAction(event -> alert.hideWithAnimation());
            layout.setActions(closeButton);
            alert.setContent(layout);
            alert.show();
        });


        ArrayList<Node> children = new ArrayList<>();
        int i = 1;
        buttons = new ArrayList<JFXButton>();
        for (Question question :
                training.getTicket().getQuestions()) {
            JFXButton button = new JFXButton();
            button.setButtonType(JFXButton.ButtonType.RAISED);
            button.setStyle("-fx-text-fill:WHITE;-fx-background-color:#5264AE;-fx-font-size:14px;");

            button.setText("" + i++);
            button.setOnMouseClicked(e -> {
                currentQuestion = Integer.parseInt(button.getText());
                setQuestionForButton(question, button);
            });
            buttons.add(button);
        }

        children.addAll(buttons);
        masonryPane.getChildren().addAll(children);

        setQuestionForButton(training.getTicket().getQuestions().get(0), buttons.get(0));

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

    private void showDialog(String body) {
        JFXAlert alert = new JFXAlert((Stage) closeButton.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label("Тренировка"));
        layout.setBody(new Label(body));
        JFXButton closeDialogButton = new JFXButton("Ок");
        closeDialogButton.getStyleClass().add("dialog-accept");
        closeDialogButton.setOnAction(event -> {
            alert.hideWithAnimation();
            Stage stage = (Stage) this.closeButton.getScene().getWindow();
            stage.close();

        });
        layout.setActions(closeDialogButton);
        alert.setContent(layout);
        alert.show();
    }

    private void setQuestionForButton(Question question, JFXButton button) {

        currentTip = question.getComment();

        textQuestion.setText(question.getText());
        textTip.setText("");

        ObservableList<Object> observableListQuestion = FXCollections.observableArrayList();
        Collections.shuffle(question.getOptions());
        observableListQuestion.setAll(question.getOptions());
        if (!button.getStyle().contains("Green") && !button.getStyle().contains("Red"))
            button.setStyle("-fx-background-color:#8d9bd7;");

        topicsListView.setItems(observableListQuestion);


        topicsListView.setCellFactory(i -> new ListCell<Option>() {
            private boolean correct = false;

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
                        setStyle(correct ? "-fx-background-color: Green;" : "-fx-background-color: Red;");
                }
            }

            @Override
            public void updateSelected(boolean selected) {
                super.updateSelected(selected);
                if (selected) {
                    setStyle(correct ? "-fx-background-color: Green;" : "-fx-background-color: Red;");
                    getItem().setChecked();
                    if (!button.getStyle().contains("Green") && !button.getStyle().contains("Red")) {
                        button.setStyle(correct ? "-fx-background-color: Green;" : "-fx-background-color: Red;");
                        if (!correct)
                            textTip.setText(question.getComment());
                        else nextQuestion();
                    }
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
        int count = (int) training.getTicket().getQuestions().stream().filter(item ->
                item.getRightOption() != (item.getOptions().stream().filter(Option::isChecked).findAny().orElse(new Option()).getId())).count();
        showDialog(String.format("Допущено ошибок: %s\nВремя: %s", count, timerLabel.getText()));
    }

    public void start(Stage window, int number) throws Exception {


    }

    public void nextQuestion() {
        currentQuestion++;
        if (currentQuestion <= training.getTicket().getQuestions().size())
            setQuestionForButton(training.getTicket().getQuestions().get(currentQuestion - 1), buttons.get(currentQuestion - 1));

    }
}
