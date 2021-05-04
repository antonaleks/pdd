package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.JFXListView;
import io.datafx.controller.ViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.Question;
import org.antonaleks.pdd.entity.Ticket;
import org.antonaleks.pdd.entity.Topic;
import org.antonaleks.pdd.model.Category;
import org.antonaleks.pdd.utils.PropertiesManager;

import java.util.List;

@ViewController(value = "/fxml/Main.fxml", title = "Material Design Example")
public class MainFormController extends BaseController {
    @FXML
    public JFXListView topicsListView;

    @FXML
    public void initialize(int ticketNumber) {


    }

    public void start(Stage window, int i) throws Exception {

        Ticket ticket = MongoHelper.getInstance().getTicketByNumber(i, Category.AB);
        ObservableList<Object> observableList = FXCollections.observableArrayList();

        observableList.setAll(ticket.getQuestions());
        topicsListView.setItems(observableList);

//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main2.fxml"));
//        Scene scene =  new Scene(root);
//        window.setScene(scene);
        window.show();
    }
}
