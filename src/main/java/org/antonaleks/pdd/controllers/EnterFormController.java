package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTooltip;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.animation.Transition;
import javafx.collections.FXCollections;
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
import org.antonaleks.pdd.entity.Session;
import org.antonaleks.pdd.entity.User;
import org.antonaleks.pdd.model.Category;
import org.antonaleks.pdd.utils.PropertiesManager;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

import static io.datafx.controller.flow.container.ContainerAnimations.SWIPE_LEFT;

@ViewController(value = "/fxml/EnterLoginForm.fxml")
public class EnterFormController extends BaseController {
    @FXML
    public JFXButton enterButton;
    @FXML
    private TextField loginField;
    @FXML
    public JFXComboBox<Category> categoryBox;
    @FXML
    private PasswordField passwordField;

    @PostConstruct
    public void init() throws Exception {
        categoryBox.setItems(FXCollections.observableArrayList(Category.AB, Category.CD));
    }

    public void buttonEnter(ActionEvent actionEvent) throws IOException {
        User newUser = new User(loginField.getText(), passwordField.getText());
        List<User> userList = MongoHelper.getInstance().getDocumentList(User.class, PropertiesManager.getDbCollectionUser());
        User currentUser = userList.stream().filter(user -> user.equals(newUser)).findFirst().orElse(null);

        if (currentUser != null) {
            Session.getInstance().init(currentUser, categoryBox.getValue());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            MainController mainController = loader.getController();

            try {
                mainController.init();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            loadModalWindow("Тренировка", root);
            Stage currentStage = (Stage) enterButton.getScene().getWindow();
            currentStage.close();

        } else {
            loginField.getStyleClass().add("wrong-credentials");
            passwordField.getStyleClass().add("wrong-credentials");
        }
    }
}
