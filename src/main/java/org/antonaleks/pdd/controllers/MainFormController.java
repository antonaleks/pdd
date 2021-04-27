package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.Topic;
import org.antonaleks.pdd.utils.PropertiesManager;

import java.util.List;

public class MainFormController extends BaseController {

    public JFXListView topicsListView;

    @FXML
    public void initialize() {
        List<Topic> topics = MongoHelper.getInstance().getDocumentList(Topic.class, PropertiesManager.getDbCollectionTopics());
        ObservableList<Object> observableList = FXCollections.observableArrayList();

        observableList.setAll(topics);
        topicsListView.setItems(observableList);
    }

    public void start(Stage window) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Scene scene =  new Scene(root);
        window.setScene(scene);
        window.show();
    }
}
