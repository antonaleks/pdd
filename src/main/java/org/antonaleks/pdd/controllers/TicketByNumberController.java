package org.antonaleks.pdd.controllers;

import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXScrollPane;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.Question;
import org.antonaleks.pdd.utils.PropertiesManager;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ViewController(value = "/fxml/TicketsByNumber.fxml", title = "PDD")
public class TicketByNumberController extends BaseController{

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private JFXMasonryPane masonryPane;
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @PostConstruct
    public void init() {
        Objects.requireNonNull(context, "context");
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");

        ArrayList<Node> children = new ArrayList<>();
        List<Question> questions = MongoHelper.getInstance().getDocumentList(Question.class, PropertiesManager.getDbCollectionQuestion());
        ObservableList<Object> observableList = FXCollections.observableArrayList();

        observableList.setAll(questions);

        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");

        for (int i =1;i<= MongoHelper.getInstance().getMaxTicket();i++) {
            JFXButton button  =new JFXButton();

            button.setStyle("-fx-text-fill:WHITE;-fx-background-color:#5264AE;-fx-font-size:14px;");
            button.setText(""+ i);

            bindNodeToController(button, TrainController.class, contentFlow, contentFlowHandler);

            children.add(button);

            button.setOnAction(e -> {

                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/fxml/main2.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                MainFormController dataController = loader.getController();

                    dataController.initialize(Integer.parseInt( button.getText()));
                loadModalWindow(e, "Data модели", root);
            });


            Timeline animation = new Timeline(new KeyFrame(Duration.millis(240)));
            animation.setDelay(Duration.millis(100 * i + 1000));
            animation.play();
        }
        masonryPane.getChildren().addAll(children);
        Platform.runLater(() -> scrollPane.requestLayout());

        JFXScrollPane.smoothScrolling(scrollPane);

    }

    private void bindNodeToController(Node node, Class<?> controllerClass, Flow flow, FlowHandler flowHandler) {
        flow.withGlobalLink(node.getId(), controllerClass);
    }

}
