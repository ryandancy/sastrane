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

import ca.keal.sastrane.config.ConfigUtils;
import ca.keal.sastrane.config.SastraneConfig;
import ca.keal.sastrane.util.I18n;
import ca.keal.sastrane.util.Resource;
import com.google.inject.Inject;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;

import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

@EqualsAndHashCode(callSuper = true)
@ToString
public class ChangeLangController extends GoBacker implements Initializable {
    
    @FXML private VBox langs;
    
    private final I18n i18n;
    private final SastraneConfig cfg;
    
    @Inject
    public ChangeLangController(I18n i18n, SastraneConfig cfg, GuiUtils guiUtils) {
        super(new Resource("ca.keal.sastrane.gui", "main-menu.fxml"), guiUtils);
        this.cfg = cfg;
        this.i18n = i18n;
    }
    
    @Override
    @SneakyThrows
    public void initialize(URL location, ResourceBundle resources) {
        // Detect all supported languages from langs.properties, add to langs flowpane
        Properties supportedLangs = new Properties();
        supportedLangs.load(new Resource("ca.keal.sastrane.i18n", "langs.properties").get().openStream());
        
        supportedLangs.forEach((langCode, name) -> {
            Hyperlink lang = new Hyperlink((String) name);
            Locale locale = Locale.forLanguageTag((String) langCode);
            if (i18n.getLocale().equals(locale)) {
                lang.getStyleClass().add("current");
            }
            lang.getStyleClass().add("lang");
            lang.setOnAction(e -> changeLang(e, locale));
            langs.getChildren().add(lang);
        });
    }
    
    @SneakyThrows
    private void changeLang(Event e, Locale locale) {
        i18n.setLocale(locale);
        goBack(e);
        
        cfg.setProperty(SastraneConfig.LOCALE_KEY, locale.toLanguageTag());
        ConfigUtils.save(cfg);
    }
    
}