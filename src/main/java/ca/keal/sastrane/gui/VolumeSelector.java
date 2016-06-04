package ca.keal.sastrane.gui;

import ca.keal.sastrane.util.Resource;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ResourceBundle;

public class VolumeSelector extends VBox implements Initializable {
    
    @FXML private Label titleLabel;
    @FXML private ImageView img;
    @FXML private Slider volume;
    
    @Getter private String title;
    
    private boolean hasInitialized = false;
    
    @SneakyThrows
    public VolumeSelector() {
        GuiUtils.loadCustom(this, new Resource("ca.keal.sastrane.gui", "volume-select.fxml"));
    }
    
    public void setTitle(String title) {
        this.title = title;
        if (hasInitialized) {
            titleLabel.setText(title);
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hasInitialized = true;
        
        titleLabel.setText(title);
        // TODO save settings, load
        volumeProperty().addListener((ov, v, nv) -> img.setImage(new Image(getVolumeIcon((double) nv).getFilename())));
        
        Platform.runLater(() -> img.setImage(new Image(getVolumeIcon(getVolume()).getFilename())));
    }
    
    private Resource getVolumeIcon(double volume) {
        if (volume == 0.0) return new Resource("ca.keal.sastrane.icon", "volume-mute.png");
        if (volume < 0.33) return new Resource("ca.keal.sastrane.icon", "volume-low.png");
        if (volume < 0.66) return new Resource("ca.keal.sastrane.icon", "volume-medium.png");
        return new Resource("ca.keal.sastrane.icon", "volume-high.png");
    }
    
    public double getVolume() {
        return volume.getValue();
    }
    
    public void setVolume(double value) {
        volume.setValue(value);
    }
    
    public DoubleProperty volumeProperty() {
        return volume.valueProperty();
    }
    
}