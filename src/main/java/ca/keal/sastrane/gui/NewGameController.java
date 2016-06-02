package ca.keal.sastrane.gui;

import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.Mover;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.event.ToGameEvent;
import ca.keal.sastrane.gui.audio.SoundEffects;
import ca.keal.sastrane.util.I18n;
import ca.keal.sastrane.util.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class NewGameController {
    
    @FXML private BorderPane container;
    @FXML private Label title;
    @FXML private FlowPane playerSettingsContainer;
    
    private Game game;
    
    public void setGame(Game game) {
        this.game = game;
        container.getStylesheets().add(game.getCss().getFilename());
        title.setText(I18n.localize("gui.newgame.title", I18n.localize(game.getI18nName())));
        playerSettingsContainer.getChildren().addAll(Arrays.stream(game.getPlayers())
                .map(PlayerSettings::new)
                .peek(settings -> settings.getStylesheets().add(game.getCss().getFilename()))
                .collect(Collectors.toList()));
    }
    
    @FXML
    @SneakyThrows
    private void onBack(ActionEvent e) {
        // Send back to the main menu
        SoundEffects.playClick();
        GuiUtils.getStage(e).setScene(GuiUtils.getScene(new Resource("ca.keal.sastrane.gui", "main-menu.fxml"),
                GuiUtils.getStage(e).getScene()));
    }
    
    @FXML
    @SneakyThrows
    private void onCreateGame(ActionEvent e) {
        SoundEffects.playClick();
        
        FXMLLoader loader = GuiUtils.getFXMLLoader(new Resource("ca.keal.sastrane.gui", "game.fxml"));
        Scene previousScene = GuiUtils.getStage(e).getScene();
        Scene scene = GuiUtils.getScene((Parent) loader.load(), previousScene);
        GameController controller = loader.getController();
        
        // Get data from player settings
        Map<Player, Mover> playersToMovers = playerSettingsContainer.getChildren().stream()
                .map(node -> (PlayerSettings) node)
                .collect(Collectors.toMap(PlayerSettings::getPlayer, settings -> {
                    if (settings.getAiOrHumanButtons().getSelectedToggle().getUserData().equals("ai")) {
                        // AI
                        return game.getAi().apply(settings.getAiDifficulty().getValue());
                    } else {
                        // Human player
                        return new HumanMover(controller);
                    }
                }));
        Round round = new Round(game, playersToMovers);
        
        round.getGame().getBus().post(new ToGameEvent.Pre(previousScene, scene, round, playersToMovers));
        controller.setRound(round);
        round.getGame().getBus().post(new ToGameEvent.Post(previousScene, scene, round, playersToMovers));
        
        GuiUtils.getStage(e).setScene(scene);
    }
    
}