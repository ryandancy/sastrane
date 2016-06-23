/*
 * Copyright (C) 2016 Ryan Dancy
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 */

package ca.keal.sastrane.gui;

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.GameAttrib;
import ca.keal.sastrane.api.GameAttribute;
import ca.keal.sastrane.api.Mover;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.event.ToGameEvent;
import ca.keal.sastrane.gui.audio.SoundEffects;
import ca.keal.sastrane.main.Main;
import ca.keal.sastrane.util.I18n;
import ca.keal.sastrane.util.Resource;
import com.google.inject.Inject;
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

@Setter
public class NewGameController extends GoBacker {
    
    @FXML @Getter private BorderPane container;
    @FXML @Getter private Label title;
    @FXML @Getter private FlowPane playerSettingsContainer;
    
    @Getter private String gameID;
    
    private final SoundEffects soundFX;
    
    private final PlayerSettings.Factory playerSettingsFactory;
    private final Round.Factory roundFactory;
    
    private final Map<String, String> i18nNames;
    private final Map<String, Resource> css;
    private final Map<String, Player[]> players;
    private final Map<String, AI.Factory> ais;
    
    @Inject
    public NewGameController(GuiUtils guiUtils, SoundEffects soundFX,
                             PlayerSettings.Factory playerSettingsFactory, Round.Factory roundFactory,
                             @GameAttribute(GameAttrib.I18N_NAME) Map<String, String> i18nNames,
                             @GameAttribute(GameAttrib.CSS) Map<String, Resource> css,
                             @GameAttribute(GameAttrib.PLAYERS) Map<String, Player[]> players,
                             @GameAttribute(GameAttrib.AI) Map<String, AI.Factory> ais) {
        super(new Resource("ca.keal.sastrane.gui", "main-menu.fxml"), guiUtils);
        this.soundFX = soundFX;
        
        this.playerSettingsFactory = playerSettingsFactory;
        this.roundFactory = roundFactory;
        
        this.i18nNames = i18nNames;
        this.css = css;
        this.players = players;
        this.ais = ais;
    }
    
    void setGame(String gameID) {
        this.gameID = gameID;
        container.getStylesheets().add(css.get(gameID).getFilename());
        title.setText(I18n.localize("gui.newgame.title", I18n.localize(i18nNames.get(gameID))));
        playerSettingsContainer.getChildren().addAll(Arrays.stream(players.get(gameID))
                .map(playerSettingsFactory::create)
                .peek(settings -> settings.getStylesheets().add(css.get(gameID).getFilename()))
                .collect(Collectors.toList()));
    }
    
    @FXML
    @SneakyThrows
    private void onCreateGame(ActionEvent e) {
        soundFX.play("click");
        
        FXMLLoader loader = guiUtils.getFXMLLoader(new Resource("ca.keal.sastrane.gui", "game.fxml"));
        Scene previousScene = Main.getStage().getScene();
        Scene scene = guiUtils.getScene((Parent) loader.load(), previousScene);
        GameController controller = loader.getController();
        
        // Get data from player settings
        Map<Player, Mover> playersToMovers = playerSettingsContainer.getChildren().stream()
                .map(node -> (PlayerSettings) node)
                .collect(Collectors.toMap(PlayerSettings::getPlayer, settings -> {
                    if (settings.getAiOrHumanButtons().getSelectedToggle().getUserData().equals("ai")) {
                        // AI
                        return ais.get(gameID).create(settings.getAiDifficulty().getValue());
                    } else {
                        // Human player
                        return new HumanMover(controller);
                    }
                }));
        Round round = roundFactory.create(gameID, playersToMovers);
        
        round.getBus().post(new ToGameEvent.Pre(previousScene, scene, round, playersToMovers));
        controller.setRound(round);
        round.getBus().post(new ToGameEvent.Post(previousScene, scene, round, playersToMovers));
        
        Main.getStage().setScene(scene);
    }
    
}