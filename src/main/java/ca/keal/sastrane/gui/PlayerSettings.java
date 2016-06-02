package ca.keal.sastrane.gui;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.gui.audio.SoundEffects;
import ca.keal.sastrane.util.I18n;
import ca.keal.sastrane.util.Resource;
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
public class PlayerSettings extends VBox implements Initializable {
    
    @FXML private ImageView img;
    @FXML private Label playerName;
    @FXML private ToggleGroup aiOrHumanButtons;
    @FXML private Slider aiDifficulty;
    
    private final Player player;
    
    @SneakyThrows
    public PlayerSettings(Player player) {
        this.player = player;
        GuiUtils.loadCustom(this, new Resource("ca.keal.sastrane.gui", "player-settings.fxml"));
    }
    
    @Override
    @SneakyThrows
    public void initialize(URL location, ResourceBundle resources) {
        img.setImage(new Image(player.getIcon().get().openStream()));
        playerName.setText(I18n.localize(player.getI18nName()));
        
        aiOrHumanButtons.selectedToggleProperty().addListener((ov, toggle, newToggle) -> {
            SoundEffects.playClick();
            if (newToggle == null) {
                toggle.setSelected(true);
            }
        });
    }
    
}