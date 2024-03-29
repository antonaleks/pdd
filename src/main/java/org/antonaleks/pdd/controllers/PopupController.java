package org.antonaleks.pdd.controllers;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;
import com.jfoenix.controls.JFXRippler;
import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Objects;

@ViewController(value = "/fxml/Popups.fxml", title = "PDD")
public final class PopupController {

    @FXML
    private StackPane root;
    @FXML
    private JFXRippler rippler1;
    @FXML
    private JFXRippler rippler2;
    @FXML
    private JFXRippler rippler3;
    @FXML
    private JFXRippler rippler4;
    @FXML
    private JFXHamburger burger1;
    @FXML
    private JFXHamburger burger2;
    @FXML
    private JFXHamburger burger3;
    @FXML
    private JFXHamburger burger4;
    @FXML
    private JFXHamburger burger5;

    private JFXPopup popup;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        try {
            popup = new JFXPopup(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/popup/DemoPopup.fxml"))));
        } catch (IOException ioExc) {
            ioExc.printStackTrace();
        }
        burger1.setOnMouseClicked((e) -> popup.show(rippler1, PopupVPosition.TOP, PopupHPosition.LEFT));
        burger2.setOnMouseClicked((e) -> popup.show(rippler2, PopupVPosition.TOP, PopupHPosition.RIGHT));
        burger3.setOnMouseClicked((e) -> popup.show(rippler3, PopupVPosition.BOTTOM, PopupHPosition.LEFT));
        burger4.setOnMouseClicked((e) -> popup.show(rippler4, PopupVPosition.BOTTOM, PopupHPosition.RIGHT));
    }
}
