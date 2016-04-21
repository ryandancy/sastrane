package ca.keal.sastrane.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO extract into utility method
        URL fxml = getClass().getClassLoader().getResource("ca/keal/sastrane/gui/main-menu.fxml");
        if (fxml == null) {
            // this shouldn't happen
            throw new RuntimeException("ca/keal/sastrane/gui/main-menu.fxml could not be found");
        }
        
        Parent root = FXMLLoader.load(fxml);
        Scene mainMenu = new Scene(root, 400, 400); // TODO stop hard-coding dimensions as 400x400; dynamic title?
        primaryStage.setTitle("Sastrane");
        primaryStage.setScene(mainMenu);
        primaryStage.show();
    }
    
}