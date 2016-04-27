package ca.keal.sastrane.gui;

import ca.keal.sastrane.Game;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainMenuController implements Initializable {
    
    @FXML
    private FlowPane tiles;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tiles.getChildren().addAll(Game.getGames().stream()
                .map(GameTile::new)
                .peek(tile -> tile.setOnMouseClicked(this::handleTileClick))
                .collect(Collectors.toList()));
    }
    
    private void handleTileClick(MouseEvent e) {
        // TODO send to settings screen
    }
    
}