package ca.keal.sastrane.gui;

import ca.keal.sastrane.gui.audio.Music;
import ca.keal.sastrane.gui.audio.SoundEffects;
import ca.keal.sastrane.util.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    
    @FXML private VolumeSelector soundfxVolume;
    @FXML private VolumeSelector musicVolume;
    
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
    @SneakyThrows
    private void onBack(ActionEvent e) {
        // Back to the main menu
        SoundEffects.play("click");
        GuiUtils.getStage(e).setScene(GuiUtils.getScene(new Resource("ca.keal.sastrane.gui", "main-menu.fxml"),
                GuiUtils.getStage(e).getScene()));
    }
    
    @FXML
    @SneakyThrows
    private void onChangeLang(ActionEvent e) {
        // To the change-lang screen
        SoundEffects.play("click");
        GuiUtils.getStage(e).setScene(GuiUtils.getScene(new Resource("ca.keal.sastrane.gui", "change-lang.fxml"),
                GuiUtils.getStage(e).getScene()));
    }
    
}