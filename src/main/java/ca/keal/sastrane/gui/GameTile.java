package ca.keal.sastrane.gui;

import ca.keal.sastrane.Game;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ca/keal/sastrane/gui/game-tile.fxml"));
        loader.setRoot(this);
        loader.load();
    }
    
    public static GameTile forGame(Game game) {
        GameTile tile = new GameTile();
        tile.setImg(new Image(game.getIconURL()));
        tile.setDisplayName(new Label(game.getName()));
        return tile;
    }
    
}