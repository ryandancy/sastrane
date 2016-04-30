package ca.keal.sastrane.gui;

import ca.keal.sastrane.Game;
import ca.keal.sastrane.Mover;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.Round;
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
        title.setText("New " + game.getName() + " Game");
        playerSettingsContainer.getChildren().addAll(Arrays.stream(game.getPlayers())
                .map(PlayerSettings::new)
                .peek(settings -> settings.getStylesheets().add(game.getCss().getFilename()))
                .collect(Collectors.toList()));
    }
    
    @FXML
    @SneakyThrows
    private void onBack(ActionEvent e) {
        // Send back to the main menu
        GuiUtils.getStage(e).setScene(GuiUtils.getScene(new Resource("ca.keal.sastrane.gui", "main-menu.fxml")));
    }
    
    @FXML
    @SneakyThrows
    private void onCreateGame(ActionEvent e) {
        FXMLLoader loader = new FXMLLoader(new Resource("ca.keal.sastrane.gui", "game.fxml").get());
        Scene scene = GuiUtils.getScene((Parent) loader.load());
        
        // Get data from player settings
        Map<Player, Mover> playersToMovers = playerSettingsContainer.getChildren().stream()
                .map(node -> (PlayerSettings) node)
                .collect(Collectors.toMap(PlayerSettings::getPlayer, settings -> {
                    if (settings.getAiOrHumanButtons().getSelectedToggle().getUserData().equals("ai")) {
                        // AI
                        return game.getAi().apply(settings.getAiDifficulty().getValue());
                    } else {
                        // Human player
                        // TODO human mover
                        return null;
                    }
                }));
        
        ((GameController) loader.getController()).setRound(new Round(game, playersToMovers));
        GuiUtils.getStage(e).setScene(scene);
    }
    
}