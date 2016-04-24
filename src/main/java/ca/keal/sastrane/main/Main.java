package ca.keal.sastrane.main;

import ca.keal.sastrane.Game;
import ca.keal.sastrane.gui.GuiUtils;
import com.google.common.reflect.ClassPath;
import javafx.application.Application;
import javafx.stage.Stage;

import java.lang.reflect.Modifier;

public class Main extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO splash screen
        
        // Load all classes, find all subclasses of Game, register each
        // @Game annotation???
        for (ClassPath.ClassInfo classInfo : ClassPath.from(getClass().getClassLoader()).getAllClasses()) {
            Class<?> cls = classInfo.load();
            if (Game.class.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
                @SuppressWarnings("unchecked") Game game = ((Class<? extends Game>) cls).newInstance();
                Game.registerGame(game);
            }
        }
        
        // TODO dynamic title
        primaryStage.setTitle("Sastrane");
        primaryStage.setScene(GuiUtils.getScene("main-menu.fxml", getClass().getClassLoader()));
        primaryStage.show();
    }
    
}