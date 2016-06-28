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
import ca.keal.sastrane.util.Resource;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

@FXMLComponent
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class GameTile extends VBox implements Initializable {
    
    @FXML private ImageView imgView;
    @FXML private Label displayName;
    
    private final String gameID;
    
    private final Map<String, String> i18nNames;
    private final Map<String, Resource> icons;
    
    @Inject
    @SneakyThrows
    GameTile(@Assisted String gameID, @GameAttribute(GameAttr.I18N_NAME) Map<String, String> i18nNames,
             @GameAttribute(GameAttr.ICON) Map<String, Resource> icons,
             @GameAttribute(GameAttr.CSS) Map<String, Resource> css, GuiUtils guiUtils) {
        this.gameID = gameID;
        this.i18nNames = i18nNames;
        this.icons = icons;
        guiUtils.loadCustom(this, new Resource("ca.keal.sastrane.gui", "game-tile.fxml"));
        getStylesheets().add(css.get(gameID).getFilename());
    }
    
    @Override
    @SneakyThrows
    public void initialize(URL location, ResourceBundle resources) {
        imgView.setImage(new Image(icons.get(gameID).get().openStream()));
        displayName.setText(resources.getString(i18nNames.get(gameID)));
    }
    
    interface Factory {
        GameTile create(String gameID);
    }
    
}