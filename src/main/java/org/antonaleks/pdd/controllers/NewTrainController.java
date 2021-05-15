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
import org.antonaleks.pdd.entity.Topic;
import org.antonaleks.pdd.model.Category;
import org.antonaleks.pdd.model.Training;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

//@ViewController(value = "/fxml/Main.fxml", title = "PDD")
public class NewTrainController extends BaseController {
    @FXML
    public JFXListView topicsListView;
    @FXML
    public JFXMasonryPane masonryPane;
    @FXML
    public Label textQuestion;
    public ImageView imageView;
    public StackPane buttonStackPane;

    private static final int STARTTIME = 0;
    public Label timerLabel;
    public JFXDialog dialog;
    public JFXButton alertButton;
    public JFXButton closeButton;
    private String currentTip;
    private List<JFXButton> buttons;
    private int currentQuestion;
    private Training training;

    @FXML
    public void initialize(int numberTicket) throws IOException {
        training = new Training(numberTicket, Category.AB);
        setInitProps(training);
    }

    @FXML
    public void initialize(Topic topic) throws IOException {
        training = new Training(topic, Category.AB);
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

        setQuestionForButton(training.getTicket().getQuestions().get(0), buttons.get(0));

        AtomicInteger time = new AtomicInteger();
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1), //1000 мс * 60 сек = 1 мин
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

        currentTip = question.getComment();

        textQuestion.setText(question.getText());


//        if (!history.containsKey(button.getText())) {
        ObservableList<Object> observableListQuestion = FXCollections.observableArrayList();
        observableListQuestion.setAll(question.getOptions());

        topicsListView.setItems(observableListQuestion);


        topicsListView.setCellFactory(i -> new ListCell<Option>() {
            private boolean correct = false;

            @Override
            public void updateItem(Option item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    Text text = new Text(item.toString());
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
                    if (!button.getStyle().contains("Green") && !button.getStyle().contains("Red"))
                        button.setStyle(correct ? "-fx-background-color: Green;" : "-fx-background-color: Red;");
//                    train.checkAnswer(question.getId(), );
                }
            }
        });

//            history.put(button.getText(), topicsListView);
//        }
//        else
//            topicsListView=history.get(button.getText());
        // TODO: Добавить сохранение выбора
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

    public void nextQuestion(ActionEvent actionEvent) {
        currentQuestion++;
        if (currentQuestion <= training.getTicket().getQuestions().size())
            setQuestionForButton(training.getTicket().getQuestions().get(currentQuestion - 1), buttons.get(currentQuestion - 1));

    }
}
