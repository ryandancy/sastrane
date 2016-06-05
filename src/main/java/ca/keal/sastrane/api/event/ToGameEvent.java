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

package ca.keal.sastrane.api.event;

import ca.keal.sastrane.api.Mover;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import javafx.scene.Scene;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * Posted when something to do with the transition to the game screen happens.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ToGameEvent extends NewSceneEvent {
    
    private final Round round;
    private final Map<Player, Mover> playersToMovers;
    
    public ToGameEvent(Scene previousScene, Scene nextScene, Round round, Map<Player, Mover> playersToMovers) {
        super(previousScene, nextScene);
        this.round = round;
        this.playersToMovers = playersToMovers;
    }
    
    public Scene getGameScene() {
        return getNextScene();
    }
    
    /**
     * Posted at the transition to the new game screen, <i>before</i> {@link
     * ca.keal.sastrane.gui.GameController#setRound(Round)} is called.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Pre extends ToGameEvent {
        
        public Pre(Scene previousScene, Scene nextScene, Round round, Map<Player, Mover> playersToMovers) {
            super(previousScene, nextScene, round, playersToMovers);
        }
        
    }
    
    /**
     * Posted at the transition to the new game screen, <i>after</i> {@link
     * ca.keal.sastrane.gui.GameController#setRound(Round)} is called.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Post extends ToGameEvent {
        
        public Post(Scene previousScene, Scene nextScene, Round round, Map<Player, Mover> playersToMovers) {
            super(previousScene, nextScene, round, playersToMovers);
        }
        
    }
    
}