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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

public abstract class Game {
    
    protected abstract Game.Name getName();
    
    protected abstract Game.Package getPackageName();
    
    public String getResourceBundleName() {
        return getPackageName() + ".i18n." + getName();
    }
    
    public String getI18nName() {
        return getName() + ".name";
    }
    
    public Resource getIcon() {
        return new Resource(getPackageName().getPkg(), getName() + ".png");
    }
    
    public Resource getCss() {
        return new Resource(getPackageName().getPkg(), getName() + ".css");
    }
    
    public abstract Player[] getPlayers();
    
    /** Difficulty to AI object */
    public abstract Function<Double, AI> getAI();
    
    public abstract Board.Factory getBoardFactory();
    
    public PlacingPiece[] getPlacingPieces() {
        return new PlacingPiece[0];
    }
    
    public boolean isPlaceOnly() {
        return getPlacingPieces().length > 0;
    }
    
    /** Register everything with the bus that should always be registered */
    public void registerDefaults(EventBus bus) {}
    
    public abstract Arbitrator getArbitrator();
    
    /** Simple wrapper of String for game names */
    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Name {
        
        private final String name;
    
        @Override
        public String toString() {
            return name;
        }
        
    }
    
    /** Simple wrapper of String for game packages */
    @Getter
    @RequiredArgsConstructor
    @EqualsAndHashCode
    public static class Package {
        
        private final String pkg;
    
        @Override
        public String toString() {
            return pkg;
        }
        
    }
    
}