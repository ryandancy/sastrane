package ca.keal.sastrane.gui;

import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.event.ToNewGameScreenEvent;
import ca.keal.sastrane.gui.audio.SoundEffects;
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
        SoundEffects.play("click");
        
        FXMLLoader loader = GuiUtils.getFXMLLoader(new Resource("ca.keal.sastrane.gui", "new-game.fxml"));
        Scene previousScene = GuiUtils.getStage(e).getScene();
        Scene scene = GuiUtils.getScene((Parent) loader.load(), previousScene);
        
        Game game = ((GameTile) e.getSource()).getGame();
        
        game.getBus().post(new ToNewGameScreenEvent.Pre(previousScene, scene, game));
        ((NewGameController) loader.getController()).setGame(game);
        game.getBus().post(new ToNewGameScreenEvent.Post(previousScene, scene, game));
        
        GuiUtils.getStage(e).setScene(scene);
    }
    
    @FXML
    @SneakyThrows
    private void onChangeLanguage(MouseEvent e) {
        SoundEffects.play("click");
        GuiUtils.getStage(e).setScene(GuiUtils.getScene(new Resource("ca.keal.sastrane.gui", "change-lang.fxml"),
                GuiUtils.getStage(e).getScene()));
    }
    
    @FXML
    @SneakyThrows
    private void onSettings(MouseEvent e) {
        SoundEffects.play("click");
        GuiUtils.getStage(e).setScene(GuiUtils.getScene(new Resource("ca.keal.sastrane.gui", "settings.fxml"),
                GuiUtils.getStage(e).getScene()));
    }
    
}