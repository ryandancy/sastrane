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

import ca.keal.sastrane.api.Round;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Root class for all events that occur in-game.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoundEvent extends RootEvent {
    
    private final Round round;
    
    /**
     * RoundEvent posted just before the first turn of a round.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Pre extends RoundEvent {
        
        public Pre(Round round) {
            super(round);
        }
        
    }
    
    /**
     * RoundEvent posted just after a round ends.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Post extends RoundEvent {
        
        public Post(Round round) {
            super(round);
        }
        
    }
    
}