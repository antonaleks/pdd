package org.antonaleks.pdd.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.User;
import org.antonaleks.pdd.utils.PropertiesManager;

import java.io.IOException;

public class EnterFormController extends BaseController {
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;


    public void buttonEnter(ActionEvent actionEvent) throws IOException {
        var newUser = new User(loginField.getText(), passwordField.getText());
        var userList = MongoHelper.getInstance().getDocumentList(User.class, PropertiesManager.getDbCollectionUser());
        var currentUser = userList.stream().filter(user -> user.equals(newUser)).findFirst().orElse(null);

        if (currentUser != null) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
            loadModalWindow(actionEvent, "Главное меню", root);
        } else {
            loginField.getStyleClass().add("wrong-credentials");
            passwordField.getStyleClass().add("wrong-credentials");
        }
    }
}
