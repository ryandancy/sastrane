package ca.keal.sastrane.gui;

import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.util.Resource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import lombok.SneakyThrows;

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
    
    @SneakyThrows
    private void handleTileClick(MouseEvent e) {
        FXMLLoader loader = new FXMLLoader(new Resource("ca.keal.sastrane.gui", "new-game.fxml").get());
        Scene scene = GuiUtils.getScene((Parent) loader.load());
        ((NewGameController) loader.getController()).setGame(((GameTile) e.getSource()).getGame());
        GuiUtils.getStage(e).setScene(scene);
    }
    
}