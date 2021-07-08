package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.*;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.antonaleks.pdd.db.MongoHelper;
import org.antonaleks.pdd.entity.Session;
import org.antonaleks.pdd.entity.User;
import org.antonaleks.pdd.model.Category;
import org.antonaleks.pdd.utils.PropertiesManager;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

@ViewController(value = "/fxml/EnterLoginForm.fxml")
public class EnterFormController extends BaseController {
    @FXML
    public JFXButton enterButton;
    @FXML
    private JFXComboBox<String> loginField;
    @FXML
    private JFXTextField licenseField;
    @FXML
    private Label wrongLicense;
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private StackPane stackPane;
    @FXML
    public JFXComboBox<Category> categoryBox;
    @FXML
    public JFXButton updateLicenseButton;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private JFXDialog dialogLicense;

    @PostConstruct
    public void init() throws Exception {
        errorLabel.setVisible(false);
//        JFXComboBox.focusedProperty().addListener((o, oldVal, newVal) -> {
//            if (!newVal) {
//                JFXComboBox.validate();
//            }
//        });
        final ObservableList<String> dummyData = FXCollections.observableArrayList();
        List<String> userList = MongoHelper.getInstance().getUserList(eq("role", "USERS"), null).stream().map(i -> i.getName()).collect(Collectors.toList());//MongoHelper.getInstance().getDocumentList(User.class, PropertiesManager.getDbCollectionUser());
        dummyData.addAll(userList);

        loginField.setItems(dummyData);
        loginField.setValue(dummyData.stream().findFirst().orElse(""));

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
        loginField.setPrefSize(passwordField.getPrefWidth(), passwordField.getPrefHeight());

        categoryBox.setItems(FXCollections.observableArrayList(Category.AB, Category.CD));
        categoryBox.getSelectionModel().select(Category.AB);
    }

    public void buttonEnter(ActionEvent actionEvent) throws IOException {

        if (MongoHelper.getInstance().checkLicense()) {

            User newUser = new User(loginField.getValue(), passwordField.getText());
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
                loadModalWindow(PropertiesManager.getAppTitle(), root);
                Stage currentStage = (Stage) enterButton.getScene().getWindow();
                currentStage.close();
            } else {
                errorLabel.setVisible(true);
            }
        } else {
            Stage currentStage = (Stage) enterButton.getScene().getWindow();

            dialogLicense.setTransitionType(JFXDialog.DialogTransition.RIGHT);
            dialogLicense.show(stackPane);
            updateLicenseButton.setOnAction(i -> {
                try {
                    if (MongoHelper.getInstance().activateLicense(licenseField.getText())) {
                        dialogLicense.close();
                    } else {
                        wrongLicense.visibleProperty().set(true);
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

            });
        }

    }

//    private void showDialog(String body, boolean closeExam) {
//        JFXAlert alert = new JFXAlert((Stage) closeButton.getScene().getWindow());
//        alert.initModality(Modality.APPLICATION_MODAL);
//        alert.setOverlayClose(false);
//        JFXDialogLayout layout = new JFXDialogLayout();
//        layout.setHeading(new Label("Экзамен"));
//        layout.setBody(new Label(body));
//        JFXButton closeDialogButton = new JFXButton("Ок");
//        closeDialogButton.getStyleClass().add("dialog-accept");
//        closeDialogButton.setOnAction(event -> {
//            alert.hideWithAnimation();
//            if (closeExam) {
//                Stage stage = (Stage) this.closeButton.getScene().getWindow();
//                stage.close();
//            }
//        });
//        layout.setActions(closeDialogButton);
//        alert.setContent(layout);
//        alert.show();
//    }
}
