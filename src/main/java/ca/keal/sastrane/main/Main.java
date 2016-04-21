package ca.keal.sastrane.main;

import ca.keal.sastrane.gui.GuiUtils;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO dynamic title
        primaryStage.setTitle("Sastrane");
        // TODO stop hard-coding dimensions as 400x400
        primaryStage.setScene(GuiUtils.getScene("main-menu.fxml", 400, 400, getClass().getClassLoader()));
        primaryStage.show();
    }
    
}