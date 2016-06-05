package ca.keal.sastrane.gui;

import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.gui.audio.SoundEffects;
import ca.keal.sastrane.util.I18n;
import ca.keal.sastrane.util.Resource;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import javax.annotation.Nullable;
import java.io.IOException;

public final class GuiUtils {
    
    private GuiUtils() {
        throw new RuntimeException("ca.keal.sastrane.gui.GuiUtils is a utility class and cannot be instantiated.");
    }
    
    public static Parent loadFXML(Resource resource) throws IOException {
        return FXMLLoader.load(resource.get(), I18n.getBundle());
    }
    
    public static FXMLLoader getFXMLLoader(Resource resource) {
        return new FXMLLoader(resource.get(), I18n.getBundle());
    }
    
    public static Scene getScene(Parent parent, double width, double height) {
        return new Scene(parent, width, height);
    }
    
    public static Scene getScene(Parent parent, Scene previous) {
        return getScene(parent, previous.getWidth(), previous.getHeight());
    }
    
    public static Scene getScene(Parent parent) {
        return new Scene(parent);
    }
    
    public static Scene getScene(Resource resource, double width, double height) throws IOException {
        return getScene(loadFXML(resource), width, height);
    }
    
    public static Scene getScene(Resource resource, Scene previous) throws IOException {
        return getScene(loadFXML(resource), previous);
    }
    
    public static Scene getScene(Resource resource) throws IOException {
        return getScene(loadFXML(resource));
    }
    
    // Thanks, http://stackoverflow.com/a/12805134
    public static Stage getStage(Event e) {
        return (Stage) ((Node) e.getSource()).getScene().getWindow();
    }
    
    public static Stage getStage(Node node) {
        return (Stage) node.getScene().getWindow();
    }
    
    /**
     * For boilerplate reduction. Usecase is to go to another scene on an event.
     */
    @SneakyThrows
    public static void goTo(Resource fxml, Event e) {
        SoundEffects.play("click");
        getStage(e).setScene(getScene(fxml, getStage(e).getScene()));
    }
    
    public static void loadCustom(Node custom, Resource resource) throws IOException {
        FXMLLoader loader = GuiUtils.getFXMLLoader(resource);
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
    
    public static Node lookup(Node node, Square square) {
        return lookup(node.getScene(), square);
    }
    
    public static Node lookup(Scene scene, Square square) {
        return scene.lookup(getLookupString(square));
    }
    
    public static String getLookupString(Square square) {
        return String.format(".x%d.y%d", square.getX(), square.getY());
    }
    
}