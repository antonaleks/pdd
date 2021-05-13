package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.*;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Objects;

@ViewController(value = "/fxml/SideMenu.fxml", title = "PDD")
public class SideMenuController {

    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    @ActionTrigger("trainByNum")
    private Label trainByNum;
    @FXML
    @ActionTrigger("trainByTheme")
    private Label trainByTheme;

    @FXML
    @ActionTrigger("exam")
    private Label exam;

    @FXML
    private JFXListView<Label> sideList;
    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() throws IOException {
        Objects.requireNonNull(context, "context");
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        sideList.propagateMouseEventsToParent();
        sideList.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> {
            new Thread(()->{
                Platform.runLater(()->{
                    if (newVal != null) {
                        try {
                            contentFlowHandler.handle(newVal.getId());
                        } catch (VetoException exc) {
                            exc.printStackTrace();
                        } catch (FlowException exc) {
                            exc.printStackTrace();
                        }
                    }
                });
            }).start();
        });
        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        bindNodeToController(trainByNum, TicketByNumberController.class, contentFlow, contentFlowHandler);
        bindNodeToController(trainByTheme, TicketByTopicController.class, contentFlow, contentFlowHandler);

        bindNodeToController(exam, PopupController.class, contentFlow, contentFlowHandler);

    }

    private void bindNodeToController(Node node, Class<?> controllerClass, Flow flow, FlowHandler flowHandler) {
        flow.withGlobalLink(node.getId(), controllerClass);
    }

}
