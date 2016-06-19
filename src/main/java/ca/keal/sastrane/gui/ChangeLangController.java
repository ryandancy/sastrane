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

import ca.keal.sastrane.util.I18n;
import ca.keal.sastrane.util.Resource;
import com.google.inject.Inject;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class ChangeLangController extends GoBacker implements Initializable {
    
    @FXML private FlowPane langs;
    
    @Inject
    public ChangeLangController(GuiUtils guiUtils) {
        super(new Resource("ca.keal.sastrane.gui", "main-menu.fxml"), guiUtils);
    }
    
    @Override
    @SneakyThrows
    public void initialize(URL location, ResourceBundle resources) {
        // Detect all supported languages from langs.properties, add to langs flowpane
        Properties supportedLangs = new Properties();
        supportedLangs.load(new Resource("ca.keal.sastrane.i18n", "langs.properties").get().openStream());
        
        supportedLangs.forEach((lang, name) -> {
            Label label = new Label((String) name);
            Locale locale = new Locale((String) lang);
            if (I18n.getLocale().equals(locale)) {
                label.getStyleClass().add("current");
            }
            label.getStyleClass().add("lang");
            label.setOnMouseClicked(e -> changeLang(e, locale));
            langs.getChildren().add(label);
        });
    }
    
    private void changeLang(Event e, Locale locale) {
        I18n.setLocale(locale);
        goBack(e);
    }
    
}