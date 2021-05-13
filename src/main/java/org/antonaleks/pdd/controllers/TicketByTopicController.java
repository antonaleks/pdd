package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXScrollPane;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ViewController(value = "/fxml/TicketsByTopic.fxml", title = "PDD")
public class TicketByTopicController extends BaseController {
    @FXML
    public VBox vboxPane;
    @FXML
    private ScrollPane scrollPane;

    private List<VBox> vboxList;
    @FXMLViewFlowContext
    private ViewFlowContext context;

    @PostConstruct
    public void init() {
        vboxList = new ArrayList<VBox>();
        for (int j = 0; j < 2; j++) {

            ArrayList<Node> children = new ArrayList<>();
            for (int i = 1; i <= 40; i++) {
                JFXButton button = new JFXButton();

                button.setStyle("-fx-text-fill:WHITE;-fx-background-color:#5264AE;-fx-font-size:14px;");
                button.setText("" + i);

                children.add(button);

                button.setOnAction(e -> {

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
                        dataController.initialize(Integer.parseInt(button.getText()));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    loadModalWindow(e, "Тренировка", root);
                });


//                Timeline animation = new Timeline(new KeyFrame(Duration.millis(240)));
//                animation.setDelay(Duration.millis(100 * i + 1000));
//                animation.play();
            }
            VBox vbox = new VBox();
            vbox.getChildren().add(new Label("Hi"));
            HBox hbox = new HBox();
            JFXMasonryPane masonryPane = new JFXMasonryPane();
            masonryPane.getChildren().addAll(children);
            hbox.getChildren().addAll(masonryPane);
            vbox.getChildren().add(masonryPane);
            vboxList.add(vbox);
        }

        vboxPane.getChildren().addAll(vboxList);
        Platform.runLater(() -> scrollPane.requestLayout());

//        scrollPane.setContent(vboxPane);

        JFXScrollPane.smoothScrolling(scrollPane);

    }


}
