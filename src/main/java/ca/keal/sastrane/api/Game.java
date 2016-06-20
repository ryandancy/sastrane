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

package ca.keal.sastrane.api;

import ca.keal.sastrane.api.piece.PlacingPiece;
import ca.keal.sastrane.util.Resource;
import com.google.common.eventbus.EventBus;

import java.util.function.Function;

public interface Game {
    
    /** Also EventBus name */
    String getResourceBundleName();
    
    String getI18nName();
    
    Resource getIcon();
    
    Resource getCss();
    
    Player[] getPlayers();
    
    /** Difficulty to AI object */
    Function<Double, AI> getAI();
    
    Board.Factory getBoardFactory();
    
    default PlacingPiece[] getPlacingPieces() {
        return new PlacingPiece[] {};
    }
    
    default boolean isPlaceOnly() {
        return getPlacingPieces().length > 0;
    }
    
    /** Register everything with the bus that should always be registered */
    default void registerDefaults(EventBus bus) {}
    
    // DI here???
    Arbitrator getArbitrator();
    
}