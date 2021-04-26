package org.antonaleks.pdd.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

abstract class BaseController {

    void loadModalWindow(ActionEvent actionEvent, String title, Parent root) {
        javafx.stage.Window parentWindow = ((Node) actionEvent.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setResizable(true);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentWindow);
        stage.show();
    }

}
