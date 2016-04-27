package ca.keal.sastrane.gui;

import ca.keal.sastrane.util.Resource;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public final class GuiUtils {
    
    // Is 400x400 the best choice here???
    public static final int DEFAULT_WIDTH = 400;
    public static final int DEFAULT_HEIGHT = 400;
    
    private GuiUtils() {
        throw new RuntimeException("ca.keal.sastrane.gui.GuiUtils is a utility class and cannot be instantiated.");
    }
    
    /**
     * getFXML("foo.fxml", "com.foo.bar", getClass().getClassLoader()) gets com/foo/bar/foo.fxml.
     */
    public static Parent getFXML(Resource resource) throws IOException {
        URL fxml = resource.get();
        if (fxml == null) throw new IOException(resource + " could not be accessed.");
        return FXMLLoader.load(fxml);
    }
    
    public static Scene getScene(Resource resource, int width, int height) throws IOException {
        return new Scene(getFXML(resource), width, height);
    }
    
    public static Scene getScene(Resource resource) throws IOException {
        return getScene(resource, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    // Thanks, http://stackoverflow.com/a/12805134
    public static Stage getStage(Event e) {
        return (Stage) ((Node) e.getSource()).getScene().getWindow();
    }
    
    public static Stage getStage(Node node) {
        return (Stage) node.getScene().getWindow();
    }
    
}