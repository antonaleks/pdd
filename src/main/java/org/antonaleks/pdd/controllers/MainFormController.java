package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.Topic;
import org.antonaleks.pdd.utils.PropertiesManager;

public class MainFormController extends BaseController {

    public JFXListView topicsListView;

    @FXML
    public void initialize() {
        var topics = MongoHelper.getInstance().getDocumentList(Topic.class, PropertiesManager.getDbCollectionTopics());
        ObservableList observableList = FXCollections.observableArrayList();

        observableList.setAll(topics);
        topicsListView.setItems(observableList);
    }
}
