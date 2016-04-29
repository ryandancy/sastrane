package ca.keal.sastrane.gui;

import ca.keal.sastrane.Game;
import ca.keal.sastrane.util.Resource;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ResourceBundle;

@Getter
@Setter
public class GameTile extends VBox implements Initializable {
    
    @FXML private ImageView imgView;
    @FXML private Label displayName;
    
    private final Game game;
    
    @SneakyThrows
    public GameTile(Game game) {
        this.game = game;
        GuiUtils.loadCustom(this, new Resource("ca.keal.sastrane.gui", "game-tile.fxml"));
    }
    
    @Override
    @SneakyThrows
    public void initialize(URL location, ResourceBundle resources) {
        imgView.setImage(new Image(game.getIcon().get().openStream()));
        displayName.setText(game.getName());
    }
    
}