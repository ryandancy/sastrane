package ca.keal.sastrane.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO extract into utility method
        Parent root = FXMLLoader.load(getClass().getResource("src/main/resources/ca/keal/sastrane/gui/main-menu.fxml"));
        Scene mainMenu = new Scene(root, 400, 400); // TODO stop hard-coding dimensions as 400x400, dynamic title?
        primaryStage.setTitle("Sastrane");
        primaryStage.setScene(mainMenu);
        primaryStage.show();
    }
    
}