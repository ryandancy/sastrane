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

import ca.keal.sastrane.gui.audio.Music;
import ca.keal.sastrane.gui.audio.SoundEffects;
import ca.keal.sastrane.main.Main;
import ca.keal.sastrane.util.Resource;
import ca.keal.sastrane.util.SastraneConfig;
import ca.keal.sastrane.util.Utils;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import lombok.SneakyThrows;
import lombok.val;
import org.aeonbits.owner.ConfigCache;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends GoBacker implements Initializable {
    
    @FXML private VolumeSelector soundfxVolume;
    @FXML private VolumeSelector musicVolume;
    
    private final SastraneConfig cfg = ConfigCache.getOrCreate(SastraneConfig.class);
    
    private final SoundEffects soundFX;
    private final Music music;
    
    @Inject
    public SettingsController(Music music, SoundEffects soundFX, GuiUtils guiUtils) {
        super(new Resource("ca.keal.sastrane.gui", "main-menu.fxml"), guiUtils);
        this.soundFX = soundFX;
        this.music = music;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        soundfxVolume.volumeProperty().addListener((val, ov, nv) -> soundFX.setVolume((double) nv));
        musicVolume.volumeProperty().addListener((val, ov, nv) -> music.setVolume((double) nv));
        
        soundfxVolume.setVolume(cfg.soundFXVolume());
        musicVolume.setVolume(cfg.musicVolume());
        
        soundfxVolume.applyCss();
        soundfxVolume.layout();
        musicVolume.applyCss();
        musicVolume.layout();
    }
    
    @SneakyThrows
    private void onLeave() {
        cfg.setProperty(SastraneConfig.SOUNDFX_VOLUME_KEY, Double.toString(soundfxVolume.getVolume()));
        cfg.setProperty(SastraneConfig.MUSIC_VOLUME_KEY, Double.toString(musicVolume.getVolume()));
        
        try (val os = new FileOutputStream(Utils.openOrCreateFile("config.properties"))) {
            cfg.store(os, "Sastrane config");
        }
    }
    
    @FXML
    private void onChangeLang(ActionEvent e) {
        guiUtils.goTo(new Resource("ca.keal.sastrane.gui", "change-lang.fxml"));
        onLeave();
    }
    
    @FXML
    private void toAbout(ActionEvent e) {
        toSimpleText("text.about");
    }
    
    @FXML
    private void toLicense(ActionEvent e) {
        toSimpleText("text.license");
    }
    
    @FXML
    private void toAttribution(ActionEvent e) {
        toSimpleText("text.attribution");
    }
    
    private void toSimpleText(String base) {
        toSimpleText(base + ".title", base + ".file");
    }
    
    @SneakyThrows
    private void toSimpleText(String title, String text) {
        soundFX.play("click");
        
        FXMLLoader loader = guiUtils.getFXMLLoader(new Resource("ca.keal.sastrane.gui", "simple-text.fxml"));
        Scene previousScene = Main.getStage().getScene();
        Scene scene = guiUtils.getScene((Parent) loader.load(), previousScene);
        
        SimpleTextController controller = loader.getController();
        controller.setTitle(title);
        controller.setTextFromFile(text);
        
        Main.getStage().setScene(scene);
        onLeave();
    }
    
    @FXML
    @Override
    protected void goBack(Event e) {
        super.goBack(e);
        onLeave();
    }
    
}