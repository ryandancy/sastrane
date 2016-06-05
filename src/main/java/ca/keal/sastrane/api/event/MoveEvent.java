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
import ca.keal.sastrane.api.move.Move;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Base class for all events that occur during the movement of a piece.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MoveEvent extends TurnEvent {
    
    private final Move move;
    
    public MoveEvent(Round round, Move move) {
        super(round);
        this.move = move;
    }
    
    /**
     * MoveEvent posted just before a move.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Pre extends MoveEvent {
        
        public Pre(Round round, Move move) {
            super(round, move);
        }
        
    }
    
    /**
     * MoveEvent posted just after a move.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Post extends MoveEvent {
        
        public Post(Round round, Move move) {
            super(round, move);
        }
        
    }
    
}