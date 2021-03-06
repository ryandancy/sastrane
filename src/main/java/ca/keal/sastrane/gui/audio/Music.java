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
import ca.keal.sastrane.util.Utils;
import com.google.inject.Singleton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;

/**
 * Plays background music. Thanks, <a href="https://dzone.com/articles/javafx-2-gametutorial-part-5">Carl Dea</a>.
 */
@Singleton
@EqualsAndHashCode
@ToString
public class Music {
    
    @Nullable private MediaPlayer player = null;
    @Getter private double volume = .5;
    private boolean shuffling = false;
    
    public void setVolume(double volume) {
        if (isPlaying()) {
            //noinspection ConstantConditions
            player.setVolume(volume);
        }
        this.volume = volume;
    }
    
    public void play(Resource resource) {
        if (isPlaying()) {
            boolean oldShuffle = shuffling;
            stop();
            shuffling = oldShuffle;
        }
        player = new MediaPlayer(new Media(resource.getFullFilename()));
        player.setVolume(volume);
        player.play();
    }
    
    public void repeat(Resource resource) {
        play(resource);
        //noinspection ConstantConditions
        player.setCycleCount(Integer.MAX_VALUE);
    }
    
    /**
     * Plays the indicated music files randomly, as if on shuffle, until stopped.
     * @param resources The list of available music files.
     */
    public void shuffleAll(Resource... resources) {
        shuffling = true;
        Runnable pickNextMusic = new Runnable() {
            @Override
            public void run() {
                if (/* everyday I'm */ shuffling) {
                    play(Utils.randomChoice(resources));
                    //noinspection ConstantConditions
                    player.setOnEndOfMedia(this);
                }
            }
        };
        pickNextMusic.run();
    }
    
    public void stop() {
        if (!isPlaying()) throw new IllegalStateException("Cannot stop when not playing");
        shuffling = false;
        //noinspection ConstantConditions
        player.stop();
        player = null;
    }
    
    public boolean isPlaying() {
        return player != null;
    }
    
}