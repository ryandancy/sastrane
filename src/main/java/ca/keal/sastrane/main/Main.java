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
        primaryStage.setScene(GuiUtils.getScene("main-menu.fxml", getClass().getClassLoader()));
        primaryStage.show();
    }
    
}