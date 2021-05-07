package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXMasonryPane;
import io.datafx.controller.ViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.Question;
import org.antonaleks.pdd.entity.Ticket;
import org.antonaleks.pdd.entity.Topic;
import org.antonaleks.pdd.model.Category;
import org.antonaleks.pdd.utils.PropertiesManager;

import java.util.ArrayList;
import java.util.List;

//@ViewController(value = "/fxml/Main.fxml", title = "PDD")
public class MainFormController extends BaseController {
    @FXML
    public JFXListView topicsListView;
    @FXML
    public JFXMasonryPane masonryPane;
    @FXML
    public Label textQuestion;

    @FXML
    public void initialize(int numberTicket) {
        Ticket ticket = MongoHelper.getInstance().getTicketByNumber(numberTicket, Category.AB);
        ObservableList<Object> observableList = FXCollections.observableArrayList();

        ArrayList<Node> children = new ArrayList<>();
        int i = 1;
        for (Question question:
                ticket.getQuestions()){
            JFXButton button = new JFXButton();
            button.setStyle("-fx-text-fill:WHITE;-fx-background-color:#5264AE;-fx-font-size:8px;");
            button.setText("" + i++);
            button.setOnMouseClicked(e -> {
                ObservableList<Object> observableListQuestion = FXCollections.observableArrayList();

                textQuestion.setText(question.getText());

                observableListQuestion.setAll(question.getOptions());
                topicsListView.setItems(observableListQuestion);

                children.add(button);
            });
        }
        masonryPane.getChildren().addAll(children);
        textQuestion.setText(ticket.getQuestions().get(0).getText());

        observableList.setAll(ticket.getQuestions().get(0).getOptions());
        topicsListView.setItems(observableList);

    }

    public void start(Stage window, int number) throws Exception {



    }
}
