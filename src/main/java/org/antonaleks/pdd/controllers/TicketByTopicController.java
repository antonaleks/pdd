package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXScrollPane;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.Question;
import org.antonaleks.pdd.entity.Topic;
import org.antonaleks.pdd.model.Category;
import org.antonaleks.pdd.utils.PropertiesManager;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ViewController(value = "/fxml/TicketsByTopic.fxml", title = "PDD")
public class TicketByTopicController extends BaseController {
    @FXML
    public VBox vboxPane;
    public JFXListView topicsListView;
    @FXML
    private ScrollPane scrollPane;

    private List<VBox> vboxList;
    @FXMLViewFlowContext
    private ViewFlowContext context;

    @PostConstruct
    public void init() {
        List<Topic> topics = MongoHelper.getInstance().getDocumentList(Topic.class, PropertiesManager.getDbCollectionTopics());
        ObservableList<Object> observableListQuestion = FXCollections.observableArrayList();
        observableListQuestion.setAll(topics);

        topicsListView.setItems(observableListQuestion);

        topicsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Topic>() {

            @Override
            public void changed(ObservableValue<? extends Topic> observable, Topic oldValue, Topic newValue) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/fxml/newTrain.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                NewTrainController dataController = loader.getController();

                try {
                    dataController.initialize(newValue);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                loadModalWindow("Тренировка", root);
            }
        });



        Platform.runLater(() -> scrollPane.requestLayout());


        JFXScrollPane.smoothScrolling(scrollPane);

    }


}
