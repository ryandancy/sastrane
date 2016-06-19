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

import ca.keal.sastrane.util.Resource;
import com.google.inject.Inject;
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

class VolumeSelector extends VBox implements Initializable {
    
    @FXML private Label titleLabel;
    @FXML private ImageView img;
    @FXML private Slider volume;
    
    @Getter private String title;
    
    private boolean hasInitialized = false;
    
    @Inject
    @SneakyThrows
    VolumeSelector(GuiUtils guiUtils) {
        guiUtils.loadCustom(this, new Resource("ca.keal.sastrane.gui", "volume-select.fxml"));
    }
    
    void setTitle(String title) {
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
    
    double getVolume() {
        return volume.getValue();
    }
    
    void setVolume(double value) {
        volume.setValue(value);
    }
    
    DoubleProperty volumeProperty() {
        return volume.valueProperty();
    }
    
}