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

package ca.keal.sastrane.gui.audio;

import ca.keal.sastrane.util.Resource;
import javafx.scene.media.AudioClip;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Acts as a cache for sound effects and allows up to two effects to be played at the same time. Thanks, <a
 * href="https://dzone.com/articles/javafx-2-gametutorial-part-5">Carl Dea"</a>.
 */
public final class SoundEffects {
    
    private static final ExecutorService POOL = Executors.newFixedThreadPool(2);
    private static final Map<String, AudioClip> CACHE = new HashMap<>();
    @Getter @Setter private static double volume = .5;
    
    private SoundEffects() {
        throw new IllegalStateException("SoundEffects is a utility class and thus cannot be instantiated");
    }
    
    public static void load(String nickname, Resource resource) {
        if (!CACHE.containsKey(nickname)) {
            CACHE.put(nickname, new AudioClip(resource.getFullFilename()));
        }
    }
    
    @SneakyThrows
    public static void loadAll(Resource propertiesFile) {
        Properties nicksToNames = new Properties();
        nicksToNames.load(propertiesFile.get().openStream());
        
        nicksToNames.forEach((nick, name) -> load((String) nick, new Resource(propertiesFile.getPkg(), (String) name)));
    }
    
    public static void play(String nickname) {
        POOL.execute(() -> CACHE.get(nickname).play(volume));
    }
    
    public static void stop() {
        POOL.shutdown();
    }
    
}