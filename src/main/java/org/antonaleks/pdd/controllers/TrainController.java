package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.antonaleks.pdd.components.ImageViewPane;
import org.antonaleks.pdd.entity.Option;
import org.antonaleks.pdd.entity.Question;
import org.antonaleks.pdd.entity.Session;
import org.antonaleks.pdd.entity.Topic;
import org.antonaleks.pdd.model.Training;
import org.antonaleks.pdd.utils.PropertiesManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

//@ViewController(value = "/fxml/Main.fxml", title = "PDD")
public class TrainController extends BaseController {
    @FXML
    public JFXMasonryPane masonryPane;
    @FXML
    public Label textQuestion;
    public ScrollPane buttonStackPane;

    public Label timerLabel;
    public JFXButton alertButton;
    public JFXButton closeButton;
    public Label textTip;
    public StackPane listStackPane;
    public StackPane imageStackPane;
    public VBox vboxPane;
    public VBox vboxPaneImage;
    private String currentTip;
    private List<JFXButton> buttons;
    private int currentQuestion;
    private Training training;
    private Timeline delayForNext = new Timeline();
    public BorderPane borderPane;

    @FXML
    public void initialize(int numberTicket) throws IOException {
        super.initialize();
        training = new Training(numberTicket, Session.getInstance().getCurrentCategory());
        setInitProps(training);
    }

    @FXML
    public void initialize(Topic topic) throws IOException {
        super.initialize();
        training = new Training(topic, Session.getInstance().getCurrentCategory());
        setInitProps(training);
    }


    public void setInitProps(Training training) throws IOException {
//        vboxPane.getChildren().add(0, new Label());
        vboxPane.getChildren().add(1, new Label());
        vboxPaneImage.getChildren().add(0, new Label());

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
        buttons = new ArrayList<>();
        for (Question question :
                training.getTicket().getQuestions()) {
            JFXButton button = new JFXButton();
            button.setButtonType(JFXButton.ButtonType.RAISED);
            button.setStyle("-fx-text-fill:" + PropertiesManager.DEFAULT_TEXT_COLOR + ";-fx-background-color:" + PropertiesManager.PASSIVE_COLOR + ";-fx-font-size:14px;");


            button.setText("" + i++);
            button.setOnMouseClicked(e -> {
                if (buttons.get(currentQuestion - 1).getStyle().contains(PropertiesManager.FOCUS_COLOR))
                    buttons.get(currentQuestion - 1).setStyle("-fx-text-fill:" + PropertiesManager.DEFAULT_TEXT_COLOR + ";-fx-background-color:" + PropertiesManager.PASSIVE_COLOR + ";-fx-font-size:14px;");
                currentQuestion = Integer.parseInt(button.getText());
                setQuestionForButton(question, button);

            });

            buttons.add(button);
            children.add(button);

        }


        masonryPane.getChildren().addAll(children);


        Platform.runLater(() -> buttonStackPane.requestLayout());

        JFXScrollPane.smoothScrolling(buttonStackPane);

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
        delayForNext.pause();
        currentTip = question.getComment();

        textQuestion.setText(question.getText());
        textTip.setText("");
        textTip.setMaxWidth(width/1.1);
        textQuestion.setMaxWidth(width/1.1);

        ObservableList<Object> observableListQuestion = FXCollections.observableArrayList();
        Collections.shuffle(question.getOptions());
        observableListQuestion.setAll(question.getOptions());
        if (!button.getStyle().contains(PropertiesManager.RIGHT_BUTTON_COLOR) && !button.getStyle().contains(PropertiesManager.FAIL_BUTTON_COLOR))
            button.setStyle("-fx-background-color:" + PropertiesManager.FOCUS_COLOR + ";-fx-text-fill:" + PropertiesManager.DEFAULT_TEXT_COLOR + ";");

        JFXListView topicsListView = new JFXListView();

        topicsListView.setItems(observableListQuestion);
        topicsListView.setMinHeight(height / 6);

        topicsListView.setCellFactory(i -> new ListCell<Option>() {
            private boolean correct = false;

            @Override
            public void updateItem(Option item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    int id = getIndex() + 1;
                    correct = question.getRightOption() == item.getId();
                    setText("" + id + ". " + item);
                    setFont(Font.font(16));
                    setWrapText(true);
                    setPrefWidth(width / 2.1);


                    if (item.isChecked()) {
                        setStyle((correct ? "-fx-background-color: " + PropertiesManager.RIGHT_BUTTON_COLOR + ";" : "-fx-background-color: " + PropertiesManager.FAIL_BUTTON_COLOR + ";") + "-fx-text-fill: " + PropertiesManager.DEFAULT_TEXT_COLOR);
                    }
                }
            }

            @Override
            public void updateSelected(boolean selected) {
                super.updateSelected(selected);
                if (selected) {
                    setStyle((correct ? "-fx-background-color: " + PropertiesManager.RIGHT_BUTTON_COLOR + ";" : "-fx-background-color: " + PropertiesManager.FAIL_BUTTON_COLOR + ";") + "-fx-text-fill: " + PropertiesManager.DEFAULT_TEXT_COLOR);


                    if (!button.getStyle().contains(PropertiesManager.RIGHT_BUTTON_COLOR) && !button.getStyle().contains(PropertiesManager.FAIL_BUTTON_COLOR)) {
                        getItem().setChecked();
                        button.setStyle((correct ? "-fx-background-color: " + PropertiesManager.RIGHT_BUTTON_COLOR + ";" : "-fx-background-color: " + PropertiesManager.FAIL_BUTTON_COLOR + ";") + "-fx-text-fill: " + PropertiesManager.DEFAULT_TEXT_COLOR);
                        if (!correct)
                            textTip.setText(question.getComment());
                        else {
                            delayForNext = new Timeline(new KeyFrame(Duration.seconds(3), ev -> {
                                nextQuestion();
                            }));
                            delayForNext.setCycleCount(1);
                            if (currentQuestion < training.getTicket().getQuestions().size())
                                delayForNext.play();
                        }
                    }
                }
            }
        });
        topicsListView.autosize();
        vboxPane.getChildren().set(1, topicsListView);

        ImageView imageView = new ImageView();

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(question.getImage().getBytes()));
            Image img = new Image(bis);
            imageView.setImage(img);
            imageView.setFitHeight(1000);

            ImageViewPane viewPane = new ImageViewPane(imageView);
            viewPane.setMaxWidth(1200);
            viewPane.setMaxHeight(450);
            vboxPaneImage.getChildren().set(0, viewPane);
            vboxPaneImage.setPrefHeight(1000);


            borderPane.setCenter(null);
            borderPane.setBottom(null);
            borderPane.setCenter(vboxPaneImage);
            borderPane.setBottom(vboxPane);


        } catch (Exception ex) {
            imageView.setImage(null);
            imageView.fitHeightProperty().set(0);
            imageView.fitWidthProperty().set(0);
            vboxPaneImage.getChildren().set(0, new ImageViewPane());

            borderPane.setCenter(null);
            borderPane.setBottom(null);
            borderPane.setCenter(vboxPane);
        }


    }

    @FXML
    public void terminateTraining() {
        int count = (int) training.getTicket().getQuestions().stream().filter(item ->
                item.getRightOption() != (item.getOptions().stream().filter(Option::isChecked).findAny().orElse(new Option()).getId())).count();
        int skipped = (int) training.getTicket().getQuestions().stream().filter(i -> i.getOptions().stream().filter(Option::isChecked).findAny().orElse(null) == null).count();
        showDialog(String.format("Допущено ошибок: %s\nПропущено: %s\nВремя: %s", count - skipped, skipped, timerLabel.getText()));
    }

    public void start(Stage window, int number) throws Exception {


    }

    public void nextQuestion() {

        if (currentQuestion < buttons.size()) {
            if (buttons.get(currentQuestion - 1).getStyle().contains(PropertiesManager.FOCUS_COLOR))
                buttons.get(currentQuestion - 1).setStyle("-fx-text-fill:" + PropertiesManager.DEFAULT_TEXT_COLOR + ";-fx-background-color:" + PropertiesManager.PASSIVE_COLOR + ";-fx-font-size:14px;");

            currentQuestion++;

            setQuestionForButton(training.getTicket().getQuestions().get(currentQuestion - 1), buttons.get(currentQuestion - 1));
        } else {
            Optional<JFXButton> skippedQuestion = buttons.stream().filter(x -> x.getStyle().contains(PropertiesManager.PASSIVE_COLOR)).findFirst();
            if (skippedQuestion.isPresent()) {
                if (buttons.get(currentQuestion - 1).getStyle().contains(PropertiesManager.FOCUS_COLOR))
                    buttons.get(currentQuestion - 1).setStyle("-fx-text-fill:" + PropertiesManager.DEFAULT_TEXT_COLOR + ";-fx-background-color:" + PropertiesManager.PASSIVE_COLOR + ";-fx-font-size:14px;");

                currentQuestion = Integer.parseInt(skippedQuestion.get().getText());

                setQuestionForButton(training.getTicket().getQuestions().get(currentQuestion - 1), buttons.get(currentQuestion - 1));

            } else
                terminateTraining();
        }
    }
}

