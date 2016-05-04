package ca.keal.sastrane.main;

import ca.keal.sastrane.gui.GuiUtils;
import ca.keal.sastrane.util.Resource;
import com.google.common.reflect.ClassPath;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class Main extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO splash screen
        
        // Load all classes so that singleton Game subclass instances will be created.
        // @Game annotation???
        ClassPath.from(getClass().getClassLoader()).getAllClasses().forEach(this::initializeClass);
        
        // TODO dynamic title
        primaryStage.setTitle("Sastrane");
        // Is 400x400 the best dimens here???
        primaryStage.setScene(GuiUtils.getScene(new Resource("ca.keal.sastrane.gui", "main-menu.fxml"), 410, 410));
        primaryStage.setMinWidth(425); // This works for some reason
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }
    
    @SneakyThrows
    private void initializeClass(ClassPath.ClassInfo cls) {
        Class.forName(cls.getName());
    }
    
}