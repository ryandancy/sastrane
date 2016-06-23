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
import com.google.inject.Singleton;
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
@Singleton
public class SoundEffects {
    
    private final ExecutorService pool = Executors.newFixedThreadPool(2);
    private final Map<String, AudioClip> cache = new HashMap<>();
    @Getter @Setter private double volume = .5;
    
    public void load(String nickname, Resource resource) {
        if (!cache.containsKey(nickname)) {
            cache.put(nickname, new AudioClip(resource.getFullFilename()));
        }
    }
    
    @SneakyThrows
    public void loadAll(Resource propertiesFile) {
        Properties nicksToNames = new Properties();
        nicksToNames.load(propertiesFile.get().openStream());
        
        nicksToNames.forEach((nick, name) -> load((String) nick, new Resource(propertiesFile.getPkg(), (String) name)));
    }
    
    public void play(String nickname) {
        pool.execute(() -> cache.get(nickname).play(volume));
    }
    
    public void stop() {
        pool.shutdown();
    }
    
}