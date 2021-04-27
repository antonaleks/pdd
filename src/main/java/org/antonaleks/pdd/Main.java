package org.antonaleks.pdd;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.antonaleks.pdd.db.MongoHelper;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        MongoHelper.getInstance();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/enterForm.fxml"));
        primaryStage.setTitle("Вход в систему");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }


}

