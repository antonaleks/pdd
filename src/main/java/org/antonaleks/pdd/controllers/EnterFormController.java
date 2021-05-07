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

    private MainFormNewController openScene ;

    @PostConstruct
    public void init() throws Exception {
        // init the title hamburger icon
//        final JFXTooltip burgerTooltip = new JFXTooltip("Open drawer");



//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/enterForm.fxml"));
//        loader.setController(new InputController());
//        toolbarPopup = new JFXPopup(loader.load());

//        optionsBurger.setOnMouseClicked(e ->
//                toolbarPopup.show(optionsBurger,
//                        JFXPopup.PopupVPosition.TOP,
//                        JFXPopup.PopupHPosition.RIGHT,
//                        -12,
//                        15));
//        JFXTooltip.setVisibleDuration(Duration.millis(3000));
//        JFXTooltip.install(titleBurgerContainer, burgerTooltip, Pos.BOTTOM_CENTER);

        // create the inner flow and content
//        context = new ViewFlowContext();
        // set the default controller
//        Flow innerFlow = new Flow(ButtonController.class);
//
//        final FlowHandler flowHandler = innerFlow.createHandler(context);
//        context.register("ContentFlowHandler", flowHandler);
//        context.register("ContentFlow", innerFlow);
//        final Duration containerAnimationDuration = Duration.millis(320);
//        drawer.setContent(flowHandler.start(new ExtendedAnimatedFlowContainer(containerAnimationDuration, SWIPE_LEFT)));
//        context.register("ContentPane", drawer.getContent().get(0));

//        // side controller will add links to the content flow
//        Flow sideMenuFlow = new Flow(SideMenuController.class);
//        final FlowHandler sideMenuFlowHandler = sideMenuFlow.createHandler(context);
//        drawer.setSidePane(sideMenuFlowHandler.start(new ExtendedAnimatedFlowContainer(containerAnimationDuration,
//                SWIPE_LEFT)));
    }

    public void buttonEnter(ActionEvent actionEvent) throws IOException {
        User newUser = new User(loginField.getText(), passwordField.getText());
        List<User> userList = MongoHelper.getInstance().getDocumentList(User.class, PropertiesManager.getDbCollectionUser());
        User currentUser = userList.stream().filter(user -> user.equals(newUser)).findFirst().orElse(null);

        if (currentUser != null) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainForm.fxml"));
//            loadModalWindow(actionEvent, "Главное меню", root);
            openScene = new MainFormNewController();

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
