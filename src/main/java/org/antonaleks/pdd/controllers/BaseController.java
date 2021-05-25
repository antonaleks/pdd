package org.antonaleks.pdd.controllers;

import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyph;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

abstract class BaseController {

    void loadModalWindow(String title, Parent root) {
//        javafx.stage.Window parentWindow = ((Node) actionEvent.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        JFXDecorator decorator = new JFXDecorator(stage, root);
        decorator.setCustomMaximize(true);
        decorator.setGraphic(new SVGGlyph(""));

        stage.setTitle(title);
        double width = 1200;
        double height = 800;
        try {
            Rectangle2D bounds = Screen.getScreens().get(0).getBounds();
            width = bounds.getWidth() / 1.2;
            height = bounds.getHeight() / 1.1;
        }catch (Exception e){ }

        Scene scene = new Scene(decorator, width, height);
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                getClass().getResource("/css/jfoenix-main-demo.css").toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.initOwner(parentWindow);
        stage.setMinHeight(height);
        stage.setMinWidth(width);
        stage.show();
//        stage.setResizable(true);
//        stage.setScene(new Scene(root));
//        stage.show();
    }

}
