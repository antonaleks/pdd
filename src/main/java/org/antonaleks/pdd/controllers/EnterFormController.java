package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.User;
import org.antonaleks.pdd.utils.PropertiesManager;

import java.io.IOException;
import java.util.List;

public class EnterFormController extends BaseController {
    public JFXButton enterButton;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

    private MainFormController openScene ;



    public void buttonEnter(ActionEvent actionEvent) throws IOException {
        User newUser = new User(loginField.getText(), passwordField.getText());
        List<User> userList = MongoHelper.getInstance().getDocumentList(User.class, PropertiesManager.getDbCollectionUser());
        User currentUser = userList.stream().filter(user -> user.equals(newUser)).findFirst().orElse(null);

        if (currentUser != null) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
//            loadModalWindow(actionEvent, "Главное меню", root);
            openScene = new MainFormController();

            Stage stage = (Stage) enterButton.getScene().getWindow();
            try {
                openScene.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            loginField.getStyleClass().add("wrong-credentials");
            passwordField.getStyleClass().add("wrong-credentials");
        }
    }
}
