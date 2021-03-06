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

import ca.keal.sastrane.api.APIModule;
import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.GameRegistrar;
import ca.keal.sastrane.chess.ChessModule;
import ca.keal.sastrane.config.ConfigModule;
import ca.keal.sastrane.config.SastraneConfig;
import ca.keal.sastrane.go.GoModule;
import ca.keal.sastrane.gui.GuiModule;
import ca.keal.sastrane.gui.GuiUtils;
import ca.keal.sastrane.gui.audio.AudioModule;
import ca.keal.sastrane.gui.audio.Music;
import ca.keal.sastrane.gui.audio.SoundEffects;
import ca.keal.sastrane.reversi.ReversiModule;
import ca.keal.sastrane.tictactoe.TicTacToeModule;
import ca.keal.sastrane.util.I18n;
import ca.keal.sastrane.util.Resource;
import ca.keal.sastrane.xiangqi.XiangqiModule;
import com.cathive.fx.guice.GuiceApplication;
import com.google.inject.Inject;
import com.google.inject.Module;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.awt.SplashScreen;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@ToString
public class Main extends GuiceApplication {
    
    @Getter private static Stage stage;
    private static boolean stageSet = false;
    
    @Inject private Set<Game> games;
    @Inject private GameRegistrar registrar;
    @Inject private SastraneConfig cfg;
    @Inject private Music music;
    @Inject private SoundEffects soundFX;
    @Inject private GuiUtils guiUtils;
    @Inject private I18n i18n;
    
    private static void setStage(Stage stage) {
        if (!stageSet) {
            Main.stage = stage;
            stageSet = true;
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        setStage(primaryStage);
        
        // No-good very bad hack to kill the process when the window is closed
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        
        soundFX.setVolume(cfg.soundFXVolume());
        music.setVolume(cfg.musicVolume());
        soundFX.loadAll(new Resource("ca.keal.sastrane.audio.soundfx", "soundfx.properties"));
        music.shuffleAll(Resource.getAllFromFile(new Resource("ca.keal.sastrane.audio.music", "soundtrack.config")));
        
        i18n.setLocale(cfg.locale());
        i18n.load("ca.keal.sastrane.i18n.sastrane");
        
        // Register all the games
        games.forEach(registrar::register);
        
        guiUtils.setTitleToDefault();
        
        // Due to a bug in JavaFX, the wrong taskbar icon is chosen, so it looks blurry
        primaryStage.getIcons().addAll(
                new Image(new Resource("ca.keal.sastrane.icon", "logo-16.png").getFilename()),
                new Image(new Resource("ca.keal.sastrane.icon", "logo-32.png").getFilename()),
                new Image(new Resource("ca.keal.sastrane.icon", "logo-48.png").getFilename()),
                new Image(new Resource("ca.keal.sastrane.icon", "logo-64.png").getFilename()));
        
        primaryStage.setScene(guiUtils.getScene(new Resource("ca.keal.sastrane.gui", "main-menu.fxml"), 410, 410));
        primaryStage.setMinWidth(425); // This works for some reason
        primaryStage.setMinHeight(500);
        
        primaryStage.show();
        
        SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash != null) {
            splash.close();
        }
    }
    
    @Override
    public void init(List<Module> modules) throws Exception {
        modules.add(new APIModule());
        modules.add(new ConfigModule());
        modules.add(new GuiModule());
        modules.add(new AudioModule());
        modules.add(new ChessModule());
        modules.add(new GoModule());
        modules.add(new ReversiModule());
        modules.add(new TicTacToeModule());
        modules.add(new XiangqiModule());
    }
    
}