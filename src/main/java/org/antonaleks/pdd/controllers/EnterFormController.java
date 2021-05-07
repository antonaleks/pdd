package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTooltip;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.User;
import org.antonaleks.pdd.utils.PropertiesManager;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

import static io.datafx.controller.flow.container.ContainerAnimations.SWIPE_LEFT;

@ViewController(value = "/fxml/enterForm.fxml")
public class EnterFormController extends BaseController {
    public JFXButton enterButton;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

    @PostConstruct
    public void init() throws Exception {

    }

    public void buttonEnter(ActionEvent actionEvent) throws IOException {
        User newUser = new User(loginField.getText(), passwordField.getText());
        List<User> userList = MongoHelper.getInstance().getDocumentList(User.class, PropertiesManager.getDbCollectionUser());
        User currentUser = userList.stream().filter(user -> user.equals(newUser)).findFirst().orElse(null);

        if (currentUser != null) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainForm.fxml"));
//            loadModalWindow(actionEvent, "Главное меню", root);
//            openScene = new MainFormNewController();

            Stage stage = (Stage) enterButton.getScene().getWindow();
            try {
//                openScene.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            loginField.getStyleClass().add("wrong-credentials");
            passwordField.getStyleClass().add("wrong-credentials");
        }
    }
}
