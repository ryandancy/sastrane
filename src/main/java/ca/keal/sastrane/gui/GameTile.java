package ca.keal.sastrane.gui;

import ca.keal.sastrane.Game;
import ca.keal.sastrane.util.Resource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
@Setter
public class GameTile extends VBox {
    
    @FXML
    private Image img;
    
    @FXML
    private Label displayName;
    
    @SneakyThrows
    public GameTile() {
        FXMLLoader loader = new FXMLLoader(new Resource("ca.keal.sastrane.gui", "game-tile.fxml").get());
        loader.setRoot(this);
        loader.load();
    }
    
    @SneakyThrows
    public static GameTile forGame(Game game) {
        GameTile tile = new GameTile();
        tile.setImg(new Image(game.getIcon().get().openStream()));
        tile.setDisplayName(new Label(game.getName()));
        return tile;
    }
    
}