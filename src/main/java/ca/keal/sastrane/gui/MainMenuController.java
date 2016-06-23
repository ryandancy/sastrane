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

import ca.keal.sastrane.api.GameAttr;
import ca.keal.sastrane.api.GameAttribute;
import ca.keal.sastrane.api.GameRegistrar;
import ca.keal.sastrane.gui.audio.SoundEffects;
import ca.keal.sastrane.main.Main;
import ca.keal.sastrane.util.Resource;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainMenuController implements Initializable {
    
    @FXML private FlowPane tiles;
    
    private final GameRegistrar registrar;
    private final SoundEffects soundFX;
    private final GameTile.Factory gameTileFactory;
    private final GuiUtils guiUtils;
    
    private final Map<String, String> i18nNames;
    
    @Inject
    public MainMenuController(GameRegistrar registrar, SoundEffects soundFX, GameTile.Factory gameTileFactory,
                              GuiUtils guiUtils, @GameAttribute(GameAttr.I18N_NAME) Map<String, String> i18nNames) {
        this.registrar = registrar;
        this.soundFX = soundFX;
        this.gameTileFactory = gameTileFactory;
        this.guiUtils = guiUtils;
        this.i18nNames = i18nNames;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tiles.getChildren().addAll(registrar.stream()
                .sorted((g0, g1) -> resources.getString(i18nNames.get(g0))
                        .compareToIgnoreCase(resources.getString(i18nNames.get(g1))))
                .map(gameTileFactory::create)
                .peek(tile -> tile.setOnMouseClicked(this::handleTileClick))
                .collect(Collectors.toList()));
    }
    
    @SneakyThrows
    private void handleTileClick(MouseEvent e) {
        soundFX.play("click");
        
        FXMLLoader loader = guiUtils.getFXMLLoader(new Resource("ca.keal.sastrane.gui", "new-game.fxml"));
        Scene previousScene = Main.getStage().getScene();
        Scene scene = guiUtils.getScene((Parent) loader.load(), previousScene);
        
        String gameID = ((GameTile) e.getSource()).getGameID();
        ((NewGameController) loader.getController()).setGame(gameID);
        
        Main.getStage().setScene(scene);
    }
    
    @FXML
    @SneakyThrows
    private void onChangeLanguage(MouseEvent e) {
        guiUtils.goTo(new Resource("ca.keal.sastrane.gui", "change-lang.fxml"));
    }
    
    @FXML
    @SneakyThrows
    private void onSettings(MouseEvent e) {
        guiUtils.goTo(new Resource("ca.keal.sastrane.gui", "settings.fxml"));
    }
    
}