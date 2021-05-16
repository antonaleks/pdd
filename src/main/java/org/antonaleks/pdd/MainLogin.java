package org.antonaleks.pdd;


import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyph;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.antonaleks.pdd.controllers.EnterFormController;
import org.antonaleks.pdd.controllers.MainController;
import org.antonaleks.pdd.db.MongoHelper;


public class MainLogin extends Application {

    @FXMLViewFlowContext
    private ViewFlowContext flowContext;
    @Override
    public void start(Stage stage) throws Exception {
        MongoHelper.getInstance();

        Flow flow = new Flow(EnterFormController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        flowContext.register("Stage", stage);
        flow.createHandler(flowContext).start(container);

        JFXDecorator decorator = new JFXDecorator(stage, container.getView());
        decorator.setCustomMaximize(true);
        decorator.setGraphic(new SVGGlyph(""));

        stage.setTitle("ПДД");

        double width = 400;
        double height = 600;
//        try {
//            Rectangle2D bounds = Screen.getScreens().get(0).getBounds();
//            width = bounds.getWidth() / 2.5;
//            height = bounds.getHeight() / 1.35;
//        }catch (Exception e){ }

        Scene scene = new Scene(decorator, width, height);
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                getClass().getResource("/css/jfoenix-main-demo.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }


}

