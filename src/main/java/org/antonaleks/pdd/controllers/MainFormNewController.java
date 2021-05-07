package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.*;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;
import javafx.util.Duration;

import javax.annotation.PostConstruct;

import static io.datafx.controller.flow.container.ContainerAnimations.SWIPE_LEFT;

//@ViewController(value = "/fxml/MainForm.fxml", title = "PDD")
public class MainFormNewController extends BaseController {
    @FXML
    public JFXListView listView;
    @FXML
    private JFXDrawer drawer;
    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    public void initialize(int numberTicket) throws FlowException {
        final JFXTooltip burgerTooltip = new JFXTooltip("Open drawer");





        JFXTooltip.setVisibleDuration(Duration.millis(3000));

        // create the inner flow and content
        context = new ViewFlowContext();
        // set the default controller
        Flow innerFlow = new Flow(TrainController.class);

        final FlowHandler flowHandler = innerFlow.createHandler(context);
        context.register("ContentFlowHandler", flowHandler);
        context.register("ContentFlow", innerFlow);
        final Duration containerAnimationDuration = Duration.millis(320);
        drawer.setContent(flowHandler.start(new ExtendedAnimatedFlowContainer(containerAnimationDuration, SWIPE_LEFT)));
        context.register("ContentPane", drawer.getContent().get(0));

        // side controller will add links to the content flow
        Flow sideMenuFlow = new Flow(SideMenuController.class);
        final FlowHandler sideMenuFlowHandler = sideMenuFlow.createHandler(context);
        drawer.setSidePane(sideMenuFlowHandler.start(new ExtendedAnimatedFlowContainer(containerAnimationDuration,
                SWIPE_LEFT)));
    }

//    public void start(Stage window) throws Exception {
//
//// create the inner flow and content
//
//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainForm.fxml"));
//        Scene scene =  new Scene(root);
//        window.setScene(scene);
//        window.show();
//    }
}
