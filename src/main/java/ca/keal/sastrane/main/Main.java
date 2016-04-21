package ca.keal.sastrane.main;

import ca.keal.sastrane.RuleSet;
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
        // Load all classes, find all subclasses of RuleSet, register each
        for (ClassPath.ClassInfo classInfo : ClassPath.from(getClass().getClassLoader()).getAllClasses()) {
            Class<?> cls = classInfo.load();
            if (RuleSet.class.isAssignableFrom(cls)) {
                @SuppressWarnings("unchecked") RuleSet ruleSet = ((Class<? extends RuleSet>) cls).newInstance();
                RuleSet.registerRuleSet(ruleSet);
            }
        }
        
        // TODO dynamic title
        primaryStage.setTitle("Sastrane");
        primaryStage.setScene(GuiUtils.getScene("main-menu.fxml", getClass().getClassLoader()));
        primaryStage.show();
    }
    
}