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
import javafx.scene.control.ScrollPane;
import javafx.util.Duration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;

@ViewController(value = "/fxml/TicketsByNumber.fxml", title = "PDD")
public class TicketByNumberController extends BaseController {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private JFXMasonryPane masonryPane;
    @FXMLViewFlowContext
    private ViewFlowContext context;

    @PostConstruct
    public void init() {
        ArrayList<Node> children = new ArrayList<>();

        for (int i = 1; i <= 40; i++) {
            JFXButton button = new JFXButton();

            button.setStyle("-fx-text-fill:WHITE;-fx-background-color:#5264AE;-fx-font-size:14px;");
            button.setText("" + i);

            children.add(button);

            button.setOnAction(e -> {

                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/fxml/Train.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                TrainController dataController = loader.getController();

                try {
                    dataController.initialize(Integer.parseInt(button.getText()));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                loadModalWindow("Тренировка", root);
            });


            Timeline animation = new Timeline(new KeyFrame(Duration.millis(240)));
            animation.setDelay(Duration.millis(100 * i + 1000));
            animation.play();
        }
        masonryPane.getChildren().addAll(children);
        Platform.runLater(() -> scrollPane.requestLayout());

        JFXScrollPane.smoothScrolling(scrollPane);

    }


}
