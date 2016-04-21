package ca.keal.sastrane.gui;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public final class GuiUtil {
    
    private GuiUtil() {
        throw new RuntimeException("ca.keal.sastrane.gui.GuiUtil is a utility class and cannot be instantiated.");
    }
    
    /**
     * getFXML("foo.fxml", "com.foo.bar", getClass().getClassLoader()) gets com/foo/bar/foo.fxml.
     */
    public static Parent getFXML(String filename, String pkg, ClassLoader loader) throws IOException {
        String fqn = pkg.replace('.', '/') + "/" + filename;
        URL fxml = loader.getResource(fqn);
        if (fxml == null) throw new IOException(fqn + " could not be accessed.");
        return FXMLLoader.load(fxml);
    }
    
    /**
     * Like {@link #getFXML(String, String, ClassLoader)}, but {@code pkg} defaults to {@code ca.keal.sastrane.gui}.
     */
    public static Parent getFXML(String filename, ClassLoader loader) throws IOException {
        return getFXML(filename, "ca.keal.sastrane.gui", loader);
    }
    
    public static Scene getScene(String filename, String pkg, int width, int height, ClassLoader loader)
            throws IOException {
        return new Scene(getFXML(filename, pkg, loader), width, height);
    }
    
    public static Scene getScene(String filename, int width, int height, ClassLoader loader) throws IOException {
        return new Scene(getFXML(filename, loader), width, height);
    }
    
    // Thanks, http://stackoverflow.com/a/12805134
    public static Stage getStage(Event e) {
        return (Stage) ((Node) e.getSource()).getScene().getWindow();
    }
    
    public static Stage getStage(Node node) {
        return (Stage) node.getScene().getWindow();
    }
    
}