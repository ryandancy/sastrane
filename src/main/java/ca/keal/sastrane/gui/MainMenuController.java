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

import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.event.ToNewGameScreenEvent;
import ca.keal.sastrane.gui.audio.SoundEffects;
import ca.keal.sastrane.main.Main;
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
                .sorted((g0, g1) -> resources.getString(g0.getInfo().getI18nName())
                        .compareToIgnoreCase(resources.getString(g1.getInfo().getI18nName())))
                .map(GameTile::new)
                .peek(tile -> tile.setOnMouseClicked(this::handleTileClick))
                .collect(Collectors.toList()));
    }
    
    @SneakyThrows
    private void handleTileClick(MouseEvent e) {
        SoundEffects.play("click");
        
        FXMLLoader loader = GuiUtils.getFXMLLoader(new Resource("ca.keal.sastrane.gui", "new-game.fxml"));
        Scene previousScene = Main.getStage().getScene();
        Scene scene = GuiUtils.getScene((Parent) loader.load(), previousScene);
        
        Game game = ((GameTile) e.getSource()).getGame();
        
        game.getBus().post(new ToNewGameScreenEvent.Pre(previousScene, scene, game));
        ((NewGameController) loader.getController()).setGame(game);
        game.getBus().post(new ToNewGameScreenEvent.Post(previousScene, scene, game));
        
        Main.getStage().setScene(scene);
    }
    
    @FXML
    @SneakyThrows
    private void onChangeLanguage(MouseEvent e) {
        GuiUtils.goTo(new Resource("ca.keal.sastrane.gui", "change-lang.fxml"));
    }
    
    @FXML
    @SneakyThrows
    private void onSettings(MouseEvent e) {
        GuiUtils.goTo(new Resource("ca.keal.sastrane.gui", "settings.fxml"));
    }
    
}