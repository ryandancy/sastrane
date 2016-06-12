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

package ca.keal.sastrane.main;

import ca.keal.sastrane.gui.GuiUtils;
import ca.keal.sastrane.gui.audio.Music;
import ca.keal.sastrane.gui.audio.SoundEffects;
import ca.keal.sastrane.util.I18n;
import ca.keal.sastrane.util.Resource;
import ca.keal.sastrane.util.SastraneConfig;
import com.google.common.reflect.ClassPath;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.aeonbits.owner.ConfigCache;

public class Main extends Application {
    
    public static Stage STAGE;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO splash screen
        
        STAGE = primaryStage;
        
        // No-good very bad hack to kill the process when the window is closed
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        
        SastraneConfig cfg = ConfigCache.getOrCreate(SastraneConfig.class);
        SoundEffects.setVolume(cfg.soundFXVolume());
        Music.setVolume(cfg.musicVolume());
        
        I18n.load("ca.keal.sastrane.i18n.sastrane");
        SoundEffects.loadAll(new Resource("ca.keal.sastrane.audio.soundfx", "soundfx.properties"));
        Music.shuffleAll(Resource.getAllFromFile(new Resource("ca.keal.sastrane.audio.music", "soundtrack.config")));
        
        // Load all classes so that singleton Game subclass instances will be created.
        // @Game annotation???
        ClassPath.from(getClass().getClassLoader()).getAllClasses().forEach(this::initializeClass);
        
        GuiUtils.setTitleToDefault();
        // Multiple icon sizes???
        primaryStage.getIcons().add(new Image(new Resource("ca.keal.sastrane.icon", "logo.png").get().openStream()));
        primaryStage.setScene(GuiUtils.getScene(new Resource("ca.keal.sastrane.gui", "main-menu.fxml"), 410, 410));
        primaryStage.setMinWidth(425); // This works for some reason
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }
    
    private void initializeClass(ClassPath.ClassInfo cls) {
        try {
            Class.forName(cls.getName());
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            // TODO log
        }
    }
    
}