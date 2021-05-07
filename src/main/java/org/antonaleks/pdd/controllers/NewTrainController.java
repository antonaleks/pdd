package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXMasonryPane;
import io.datafx.controller.ViewController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.Option;
import org.antonaleks.pdd.entity.Question;
import org.antonaleks.pdd.entity.Ticket;
import org.antonaleks.pdd.entity.Topic;
import org.antonaleks.pdd.model.Category;
import org.antonaleks.pdd.model.Training;
import org.antonaleks.pdd.utils.PropertiesManager;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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

    @FXML
    public void initialize(int numberTicket) throws IOException {
        Training training = new Training(numberTicket, Category.AB);
        ArrayList<Node> children = new ArrayList<>();
        int i = 1;
        List<JFXButton> buttons = new ArrayList<JFXButton>();
        for (Question question :
                training.getTicket().getQuestions()) {
            JFXButton button = new JFXButton();
            button.setStyle("-fx-text-fill:WHITE;-fx-background-color:#5264AE;-fx-font-size:8px;");
            button.setText("" + i++);
            button.setOnMouseClicked(e -> {
                setQuestionForButton(question);


            });
            buttons.add(button);
        }

        children.addAll(buttons);
        masonryPane.getChildren().addAll(children);

        setQuestionForButton(training.getTicket().getQuestions().get(0));

    }

    private void setQuestionForButton(Question question) {
        ObservableList<Object> observableListQuestion = FXCollections.observableArrayList();

        textQuestion.setText(question.getText());

        observableListQuestion.setAll(question.getOptions());
        topicsListView.setItems(observableListQuestion);

        topicsListView.setCellFactory(i -> new ListCell<Option>() {
            private boolean correct = false;

            @Override
            public void updateItem(Option item, boolean empty) {
                super.updateItem(item, empty);
                if( item != null) {
                    correct = question.getRightOption() == item.getId();
                    setText(item.toString());
                }
            }

            @Override
            public void updateSelected(boolean selected) {
                super.updateSelected(selected);
                if (selected) {
                    setStyle(correct ? "-fx-background-color: Green;" : "-fx-background-color: Red;");
//                    train.checkAnswer(question.getId(), );
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

    public void start(Stage window, int number) throws Exception {


    }
}
