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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class Game {
    
    @Getter private final String name;
    private final String pkg;
    
    public String getPackage() { return pkg; }
    
    public String getResourceBundleName() {
        return getPackage() + ".i18n." + getName();
    }
    
    public String getI18nName() {
        return getName() + ".name";
    }
    
    public Resource getIcon() {
        return new Resource(getPackage(), getName() + ".png");
    }
    
    public Resource getCss() {
        return new Resource(getPackage(), getName() + ".css");
    }
    
    public BoardDecor[] getBoardDecor() {
        return new BoardDecor[0];
    }
    
    public abstract Player[] getPlayers();
    
    public abstract AI.Factory getAIFactory();
    
    public abstract Board.Factory getBoardFactory();
    
    public PlacingPiece[] getPlacingPieces() {
        return new PlacingPiece[0];
    }
    
    /** Override this to return false if your game has both placing and non-placing pieces */
    public boolean isPlaceOnly() {
        return getPlacingPieces().length > 0;
    }
    
    public abstract Arbitrator getArbitrator();
    
    /** null -> not notatable */
    @Nullable
    public Notater getNotater() {
        return null;
    }
    
    // TODO remove the "defaults registrator" stuff
    public Consumer<EventBus> getDefaultsRegistrator() {
        return bus -> {};
    }
    
    public boolean allowPassing() {
        return false;
    }
    
    public boolean isAutoPassingEnabled() {
        return false;
    }
    
}