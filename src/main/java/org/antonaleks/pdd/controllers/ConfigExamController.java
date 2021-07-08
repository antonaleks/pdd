package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.JFXButton;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;
import java.io.IOException;

@ViewController(value = "/fxml/ConfigExam.fxml", title = "PDD")
public class ConfigExamController extends BaseController {
    @FXML
    public VBox vboxPane;
    public JFXButton startButton;
    @FXML
    private ScrollPane scrollPane;

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @PostConstruct
    public void init() {

//        Platform.runLater(() -> scrollPane.requestLayout());
//
//
//        JFXScrollPane.smoothScrolling(scrollPane);

    }

    public void startExam() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Exam.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        ExamController dataController = loader.getController();

//        try {
//            dataController.initialize();
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        }
        loadModalWindow("Экзамен", root);
    }


}
