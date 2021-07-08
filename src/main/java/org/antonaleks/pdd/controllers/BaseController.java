package org.antonaleks.pdd.controllers;

import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyph;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

abstract class BaseController {
    protected static double width;
    protected static double height;
//    protected static Stage stage;

    @FXML
    void initialize() throws IOException {
        double default_width = 800;
        double default_height = 1000;
        try {
            Rectangle2D bounds = Screen.getScreens().get(0).getBounds();
            this.width = Math.min(bounds.getWidth() / 1.2, default_width);
            this.height = Math.min(bounds.getHeight() / 1.1, default_height);
        } catch (Exception e) {
        }
    }

    void loadModalWindow(String title, Parent root) {
//        javafx.stage.Window parentWindow = ((Node) actionEvent.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        JFXDecorator decorator = new JFXDecorator(stage, root);
        decorator.setCustomMaximize(true);
        decorator.setGraphic(new SVGGlyph(""));

        stage.setTitle(title);

        Scene scene = new Scene(decorator, this.width, this.height);
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                getClass().getResource("/css/jfoenix-main-demo.css").toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.initOwner(parentWindow);
        stage.setHeight(this.height);
        stage.setWidth(this.width);
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            height = stage.getHeight();
            width = stage.getWidth();
        };

        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);
        stage.show();
//        stage.setResizable(true);
//        stage.setScene(new Scene(root));
//        stage.show();
    }

//    protected void updateSize(){
//        height = stage.getHeight();
//        width = stage.getWidth();
//    }


}
