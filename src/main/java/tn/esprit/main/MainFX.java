package tn.esprit.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class MainFX extends Application {


    @Override
    public void start(Stage primaryStage) throws IOException {
<<<<<<< HEAD

        Parent root= FXMLLoader.load(getClass().getResource("/login.fxml"));
=======
        Parent root= FXMLLoader.load(getClass().getResource("/login.fxml"));
        //Parent root= FXMLLoader.load(getClass().getResource("/Medicalfront.fxml"));
>>>>>>> 31bb8b24b66f6d59f3f1a023668c1ae5d3a3a67a
        //Parent root= FXMLLoader.load(getClass().getResource("/AdminBack.fxml"));
        //Parent root= FXMLLoader.load(getClass().getResource("/AddClaimAthlete.fxml"));
        Scene scene=new Scene(root);
        //primaryStage.setTitle("first Scene");
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();



    }
    public static void main(String[] args) {
        launch(args);
    }
}