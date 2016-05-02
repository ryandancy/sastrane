package ca.keal.sastrane.gui;

import ca.keal.sastrane.util.Resource;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.annotation.Nullable;
import java.io.IOException;

public final class GuiUtils {
    
    // Is 400x400 the best choice here???
    public static final int DEFAULT_WIDTH = 400;
    public static final int DEFAULT_HEIGHT = 400;
    
    private GuiUtils() {
        throw new RuntimeException("ca.keal.sastrane.gui.GuiUtils is a utility class and cannot be instantiated.");
    }
    
    /**
     * loadFXML("foo.fxml", "com.foo.bar", getClass().getClassLoader()) gets com/foo/bar/foo.fxml.
     */
    public static Parent loadFXML(Resource resource) throws IOException {
        return FXMLLoader.load(resource.get());
    }
    
    public static Scene getScene(Parent parent, int width, int height) {
        return new Scene(parent, width, height);
    }
    
    public static Scene getScene(Parent parent) {
        return getScene(parent, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    public static Scene getScene(Resource resource, int width, int height) throws IOException {
        return getScene(loadFXML(resource), width, height);
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
    
    public static void loadCustom(Node custom, Resource resource) throws IOException {
        FXMLLoader loader = new FXMLLoader(resource.get());
        loader.setRoot(custom);
        loader.setController(custom);
        loader.load();
    }
    
    // http://stackoverflow.com/a/20656861
    @Nullable
    public static Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (Integer.valueOf(col).equals(GridPane.getColumnIndex(node))
                    && Integer.valueOf(row).equals(GridPane.getRowIndex(node))) {
                return node;
            }
        }
        return null;
    }
    
}