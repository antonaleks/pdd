package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.ViewController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.Session;
import org.antonaleks.pdd.entity.User;
import org.antonaleks.pdd.model.Category;
import org.antonaleks.pdd.utils.PropertiesManager;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@ViewController(value = "/fxml/EnterLoginForm.fxml")
public class EnterFormController extends BaseController {
    @FXML
    public JFXButton enterButton;
    @FXML
    private JFXTextField loginField;
    @FXML
    public JFXComboBox<Category> categoryBox;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private Label errorLabel;

    @PostConstruct
    public void init() throws Exception {
        errorLabel.setVisible(false);
        loginField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                loginField.validate();
            }
        });
        categoryBox.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                categoryBox.validate();
            }
        });
        passwordField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                passwordField.validate();
            }
        });
        categoryBox.setItems(FXCollections.observableArrayList(Category.AB, Category.CD));
        categoryBox.getSelectionModel().select(Category.AB);
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
            errorLabel.setVisible(true);

        }
    }
}
