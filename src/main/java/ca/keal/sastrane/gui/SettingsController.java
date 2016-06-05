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
import ca.keal.sastrane.util.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends GoBacker implements Initializable {
    
    @FXML private VolumeSelector soundfxVolume;
    @FXML private VolumeSelector musicVolume;
    
    public SettingsController() {
        super(new Resource("ca.keal.sastrane.gui", "main-menu.fxml"));
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        soundfxVolume.volumeProperty().addListener((val, ov, nv) -> SoundEffects.setVolume((double) nv));
        musicVolume.volumeProperty().addListener((val, ov, nv) -> Music.setVolume((double) nv));
        
        soundfxVolume.applyCss();
        soundfxVolume.layout();
        musicVolume.applyCss();
        musicVolume.layout();
    }
    
    @FXML
    private void onChangeLang(ActionEvent e) {
        GuiUtils.goTo(new Resource("ca.keal.sastrane.gui", "change-lang.fxml"), e);
    }
    
    @FXML
    private void toAbout(ActionEvent e) {
        toSimpleText("text.about", e);
    }
    
    @FXML
    private void toLicense(ActionEvent e) {
        toSimpleText("text.license", e);
    }
    
    @FXML
    private void toAttribution(ActionEvent e) {
        toSimpleText("text.attribution", e);
    }
    
    private void toSimpleText(String base, ActionEvent e) {
        toSimpleText(base + ".title", base + ".file", e);
    }
    
    @SneakyThrows
    private void toSimpleText(String title, String text, ActionEvent e) {
        SoundEffects.play("click");
        
        FXMLLoader loader = GuiUtils.getFXMLLoader(new Resource("ca.keal.sastrane.gui", "simple-text.fxml"));
        Scene previousScene = GuiUtils.getStage(e).getScene();
        Scene scene = GuiUtils.getScene((Parent) loader.load(), previousScene);
        
        SimpleTextController controller = loader.getController();
        controller.setTitle(title);
        controller.setText(text);
        
        GuiUtils.getStage(e).setScene(scene);
    }
    
}