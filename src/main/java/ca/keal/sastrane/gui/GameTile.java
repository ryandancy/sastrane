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
        getStylesheets().add(game.getInfo().getCss().getFilename());
    }
    
    @Override
    @SneakyThrows
    public void initialize(URL location, ResourceBundle resources) {
        imgView.setImage(new Image(game.getInfo().getIcon().get().openStream()));
        displayName.setText(resources.getString(game.getInfo().getI18nName()));
    }
    
}