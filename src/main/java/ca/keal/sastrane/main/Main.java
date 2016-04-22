package ca.keal.sastrane.main;

import ca.keal.sastrane.RuleSet;
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
        
        // Load all classes, find all subclasses of RuleSet, register each
        for (ClassPath.ClassInfo classInfo : ClassPath.from(getClass().getClassLoader()).getAllClasses()) {
            // Exclude lombok, guava
            String pkg = classInfo.getPackageName();
            if (!pkg.startsWith("lombok") && !pkg.startsWith("com.google.common")) {
                Class<?> cls = classInfo.load();
                if (RuleSet.class.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
                    @SuppressWarnings("unchecked") RuleSet ruleSet = ((Class<? extends RuleSet>) cls).newInstance();
                    RuleSet.registerRuleSet(ruleSet);
                }
            }
        }
        
        // TODO dynamic title
        primaryStage.setTitle("Sastrane");
        primaryStage.setScene(GuiUtils.getScene("main-menu.fxml", getClass().getClassLoader()));
        primaryStage.show();
    }
    
}