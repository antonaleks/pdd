package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
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
import org.antonaleks.pdd.entity.Statistic;
import org.antonaleks.pdd.model.Exam;
import org.antonaleks.pdd.utils.PropertiesManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

//@ViewController(value = "/fxml/Main.fxml", title = "PDD")
public class ExamController extends BaseController {
    public static final int TIME_EXAM_LIMIT = 20 * 60;
    public static final String EXAM_PASSED = "Экзамен сдан, %s ошибок\nОставшееся время: %s";
    public static final String EXAM_CONTINUE = "Получено %s ошибок, добавлены вопросы";
    public static final String EXAM_NOT_PASSED = "Экзамен не сдан, ошибок: %s\nОставшееся время: %s";
    private static final int FIVE_MINUTES = 60 * 5;
    //    @FXML
//    public JFXListView topicsListView;
    @FXML
    public JFXMasonryPane masonryPane;
    @FXML
    public Label textQuestion;
    public ImageView imageView;
    public ScrollPane buttonStackPane;
    public StackPane listStackPane;
    public VBox vboxPaneImage;
    private Timeline timeline;
    public Label timerLabel;
    public JFXButton alertButton;
    public JFXButton closeButton;
    private List<JFXButton> buttons;
    private int currentQuestion;
    private Exam exam;
    private AtomicInteger time;
    public VBox vboxPane;
    public BorderPane borderPane;

    @FXML
    public void initialize() {
        super.initialize();
        exam = new Exam(Session.getInstance().getCurrentCategory());
        setInitProps();
    }

    public void setInitProps() {
        vboxPaneImage.getChildren().add(0, new Label());
        vboxPane.getChildren().add(1, new Label());

        buttons = new ArrayList<>();
        fillButtonsQuestion(0, 20);

        setQuestionForButton(exam.getTicket().getQuestions().get(0), buttons.get(0));

        time = new AtomicInteger(TIME_EXAM_LIMIT);
        timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        ae -> {
                            time.getAndDecrement();
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
        timeline.setOnFinished(event -> {
            System.out.println("finish");
            try {
                finishExam();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        timeline.setCycleCount(TIME_EXAM_LIMIT);
        timeline.play();

        currentQuestion = 1;


    }

    private void fillButtonsQuestion(int subFrom, int subTo) {
        if (subFrom < subTo) {
            int i = subFrom + 1;
            for (Question question :
                    exam.getTicket().getQuestions().subList(subFrom, subTo)) {
                JFXButton button = new JFXButton();
                button.setStyle("-fx-text-fill:" + PropertiesManager.DEFAULT_TEXT_COLOR + ";-fx-background-color:" + PropertiesManager.PASSIVE_COLOR + ";-fx-font-size:14px;");

                button.setText("" + i++);
                button.setOnMouseClicked(e -> {
                    if (buttons.get(currentQuestion - 1).getStyle().contains(PropertiesManager.FOCUS_COLOR))
                        buttons.get(currentQuestion - 1).setStyle("-fx-text-fill:" + PropertiesManager.DEFAULT_TEXT_COLOR + ";-fx-background-color:" + PropertiesManager.PASSIVE_COLOR + ";-fx-font-size:14px;");

                    currentQuestion = Integer.parseInt(button.getText());
                    setQuestionForButton(question, button);
                });
                buttons.add(button);
            }

            ArrayList<Node> children = new ArrayList<>(buttons.subList(subFrom, subTo));
            masonryPane.getChildren().addAll(children);
            Platform.runLater(() -> buttonStackPane.requestLayout());

            JFXScrollPane.smoothScrolling(buttonStackPane);

        }
    }

    private boolean finishExam() throws IOException {
        int count = (int) exam.getTicket().getQuestions().subList(0, buttons.size()).stream().filter(item ->
                item.getRightOption() != (item.getOptions().stream().filter(Option::isChecked).findAny().orElse(new Option()).getId())).count();

        int examPassed = -1;
        if (count == 0)
            examPassed = 1;
        else if (count > 0 & count <= 2) {
            int curMistakes = ((20 + 5 * count) - buttons.size()) / 5;
            if (curMistakes != 0) {

                timeline.setCycleCount(timeline.getCycleCount() + FIVE_MINUTES * curMistakes);
                time.addAndGet(FIVE_MINUTES * curMistakes);

                showDialog(String.format(EXAM_CONTINUE, count), false);
                fillButtonsQuestion(buttons.size(), 20 + 5 * count);
            } else {
                examPassed = 1;
            }

        } else if (count > 2) {
            examPassed = 0;

        }
        if (examPassed != -1) {
            Statistic statistic = new Statistic(Timestamp.from(Instant.now()).getTime(), count, buttons.size() - count, examPassed == 1);
            Session.getInstance().getCurrentUser().addStatistic(statistic);
            Session.getInstance().getCurrentUser().updateStatistic();
            showDialog(String.format(examPassed == 1 ? EXAM_PASSED : EXAM_NOT_PASSED, count, timerLabel.getText()), true);
        }
        return false;

    }

    private void showDialog(String body, boolean closeExam) {
        JFXAlert alert = new JFXAlert((Stage) closeButton.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label("Экзамен"));
        layout.setBody(new Label(body));
        JFXButton closeDialogButton = new JFXButton("Ок");
        closeDialogButton.getStyleClass().add("dialog-accept");
        closeDialogButton.setOnAction(event -> {
            alert.hideWithAnimation();
            if (closeExam) {
                Stage stage = (Stage) this.closeButton.getScene().getWindow();
                stage.close();
            }
        });
        layout.setActions(closeDialogButton);
        alert.setContent(layout);
        alert.show();
    }

    private void setQuestionForButton(Question question, JFXButton button) {
        textQuestion.setText(question.getText());
        textQuestion.setMaxWidth(width / 1.1);


        ObservableList<Option> observableListQuestion = FXCollections.observableArrayList();
        Collections.shuffle(question.getOptions());
        observableListQuestion.setAll(question.getOptions());
        if (!button.getStyle().contains("Green") && !button.getStyle().contains("Red"))
            button.setStyle("-fx-background-color:" + PropertiesManager.FOCUS_COLOR + ";-fx-text-fill:" + PropertiesManager.DEFAULT_TEXT_COLOR + ";");

        JFXListView topicsListView = new JFXListView();

        topicsListView.setItems(observableListQuestion);
        topicsListView.setMinHeight(height / 6);


        topicsListView.setCellFactory(i -> new ListCell<Option>() {
            private boolean correct = false;
            private String defaultStyle = getStyle();

            @Override
            public void updateItem(Option item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    int id = getIndex() + 1;
                    setText("" + id + ". " + item);
                    setWrapText(true);
                    setPrefWidth(width / 2.1);
                    setFont(Font.font(16));
                    correct = question.getRightOption() == item.getId();
                    if (item.isChecked())
                        setStyle(PropertiesManager.CHECKED_EXAM_BUTTON_COLOR + "-fx-text-fill: " + PropertiesManager.DEFAULT_TEXT_COLOR);
                }
            }

            @Override
            public void updateSelected(boolean selected) {
                super.updateSelected(selected);
                if (selected) {
                    getItem().setChecked();
                    button.setDisable(true);

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
    public void terminateTraining() throws IOException {
        finishExam();
    }

    public void start(Stage window, int number) throws Exception {


    }

    public void nextQuestion() throws IOException {
        if (currentQuestion < exam.getTicket().getQuestions().size()) {


            if (currentQuestion < buttons.size()) {
                if (buttons.get(currentQuestion - 1).getStyle().contains(PropertiesManager.FOCUS_COLOR))
                    buttons.get(currentQuestion - 1).setStyle("-fx-text-fill:" + PropertiesManager.DEFAULT_TEXT_COLOR + ";-fx-background-color:" + PropertiesManager.PASSIVE_COLOR + ";-fx-font-size:14px;");
                currentQuestion++;

                if (buttons.get(currentQuestion - 1).isDisable())
                    nextQuestion();
                else
                    setQuestionForButton(exam.getTicket().getQuestions().get(currentQuestion - 1), buttons.get(currentQuestion - 1));
            } else {
                Optional<Question> skippedQuestion = exam.getTicket().getQuestions().subList(0, buttons.size()).stream().filter(item ->
                        !item.getOptions().stream().filter(Option::isChecked).findAny().isPresent()).findFirst();
                if (skippedQuestion.isPresent()) {
                    if (buttons.get(currentQuestion - 1).getStyle().contains(PropertiesManager.FOCUS_COLOR))
                        buttons.get(currentQuestion - 1).setStyle("-fx-text-fill:" + PropertiesManager.DEFAULT_TEXT_COLOR + ";-fx-background-color:" + PropertiesManager.PASSIVE_COLOR + ";-fx-font-size:14px;");

                    currentQuestion = exam.getTicket().getQuestions().indexOf(skippedQuestion.get()) + 1;

                    setQuestionForButton(exam.getTicket().getQuestions().get(currentQuestion - 1), buttons.get(currentQuestion - 1));

                } else
                    terminateTraining();
            }
        }
    }
}
