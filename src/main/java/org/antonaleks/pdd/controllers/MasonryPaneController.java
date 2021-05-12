package org.antonaleks.pdd.controllers;

import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXScrollPane;
import io.datafx.controller.ViewController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;

@ViewController(value = "/fxml/Masonrys.fxml", title = "PDD")
public class MasonryPaneController {

    public static final String BUTTON_BACKGROUND_COLOR = "#5264AE";
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private JFXMasonryPane masonryPane;
    private NewTrainController openScene ;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        ArrayList<Node> children = new ArrayList<>();
//        List<Question> questions = MongoHelper.getInstance().getDocumentList(Question.class, PropertiesManager.getDbCollectionQuestion());
//        ObservableList<Object> observableList = FXCollections.observableArrayList();
//
//        observableList.setAll(questions);


        for (int i =1;i<= 40;i++) {
            JFXButton button  =new JFXButton();
            button.setStyle("-fx-text-fill:WHITE;-fx-background-color:" + BUTTON_BACKGROUND_COLOR + ";-fx-font-size:14px;");
            button.setText(""+ i);
            children.add(button);
            button.setOnMouseClicked(e -> {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main2.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                openScene = loader.getController();
                Stage stage = (Stage) button.getScene().getWindow();
                Scene scene =  new Scene(root);
                final ObservableList<String> stylesheets = scene.getStylesheets();
                stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                        JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                        getClass().getResource("/css/jfoenix-main-demo.css").toExternalForm());
                stage.setScene(scene);
                try {
                    openScene.start(stage,Integer.parseInt(button.getText()));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
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
