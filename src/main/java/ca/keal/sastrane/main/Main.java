package ca.keal.sastrane.main;

import ca.keal.sastrane.gui.GuiUtils;
import com.google.common.reflect.ClassPath;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO splash screen
        
        // Load all classes so that singleton Game subclass instances will be created.
        // @Game annotation???
        ClassPath.from(getClass().getClassLoader()).getAllClasses().forEach(ClassPath.ClassInfo::load);
        
        // TODO dynamic title
        primaryStage.setTitle("Sastrane");
        primaryStage.setScene(GuiUtils.getScene("main-menu.fxml", getClass().getClassLoader()));
        primaryStage.show();
    }
    
}