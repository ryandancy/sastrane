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

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.gui.audio.SoundEffects;
import ca.keal.sastrane.util.Resource;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ResourceBundle;

@Getter
class PlayerSettings extends VBox implements Initializable {
    
    @FXML private ImageView img;
    @FXML private Label playerName;
    @FXML private ToggleGroup aiOrHumanButtons;
    @FXML private Slider aiDifficulty;
    
    private final Player player;
    
    private final SoundEffects soundFX;
    
    @Inject
    @SneakyThrows
    PlayerSettings(@Assisted Player player, SoundEffects soundFX, GuiUtils guiUtils) {
        this.player = player;
        this.soundFX = soundFX;
        guiUtils.loadCustom(this, new Resource("ca.keal.sastrane.gui", "player-settings.fxml"));
    }
    
    @Override
    @SneakyThrows
    public void initialize(URL location, ResourceBundle resources) {
        img.setImage(new Image(player.getIcon().get().openStream()));
        playerName.setText(resources.getString(player.getI18nName()));
        
        aiOrHumanButtons.selectedToggleProperty().addListener((ov, toggle, newToggle) -> {
            soundFX.play("click");
            if (newToggle == null) {
                toggle.setSelected(true);
            }
        });
    }
    
    /** For AssistedInject */
    interface Factory {
        PlayerSettings create(Player player);
    }
    
}