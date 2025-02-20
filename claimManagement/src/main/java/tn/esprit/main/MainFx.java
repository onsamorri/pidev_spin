package tn.esprit.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFx extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        //Parent root= FXMLLoader.load(getClass().getResource("/AddClaimInterface.fxml"));
        Parent root= FXMLLoader.load(getClass().getResource("/ManageClaimsInterface.fxml"));
<<<<<<< HEAD
        //Parent root= FXMLLoader.load(getClass().getResource("/AddClaimActionInterface.fxml"));
        //Parent root= FXMLLoader.load(getClass().getResource("/ManageClaimActionsInterface.fxml"));
=======
>>>>>>> 25b00d9d2a12642a04c47dfc83611927232072c4

        Scene scene=new Scene(root);

        primaryStage.setScene(scene);

        primaryStage.show();

        primaryStage.setTitle("Claims");


    }

    public static void main(String[] args) {
        launch(args);
    }
}
