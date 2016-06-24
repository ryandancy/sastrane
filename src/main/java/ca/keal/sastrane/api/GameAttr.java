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
import com.google.inject.TypeLiteral;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Consumer;

/** The attributes for {@link GameAttribute @GameAttribute}. GAAAH NAMING TROUBLES GAAAAH */
@AllArgsConstructor
@NoArgsConstructor
public enum GameAttr {
    
    NAME,
    PACKAGE,
    RESOURCE_BUNDLE_NAME {
        @Override
        protected GameAttr[] getAutoAddDependencies() {
            return new GameAttr[] {NAME, PACKAGE};
        }
        
        @Override
        protected void autoAdd(Map<GameAttr, PossiblyTypedValue<?>> attribs) {
            attribs.put(this, new PossiblyTypedValue<>(attribs.get(PACKAGE) + ".i18n." + attribs.get(NAME)));
        }
    },
    I18N_NAME {
        @Override
        protected GameAttr[] getAutoAddDependencies() {
            return new GameAttr[] {NAME};
        }
        
        @Override
        protected void autoAdd(Map<GameAttr, PossiblyTypedValue<?>> attribs) {
            attribs.put(this, new PossiblyTypedValue<>(attribs.get(NAME) + ".name"));
        }
    },
    ICON {
        @Override
        protected GameAttr[] getAutoAddDependencies() {
            return new GameAttr[] {NAME, PACKAGE};
        }
        
        @Override
        @SuppressWarnings("ConstantConditions")
        protected void autoAdd(Map<GameAttr, PossiblyTypedValue<?>> attribs) {
            attribs.put(this, new PossiblyTypedValue<>(new Resource(
                    (String) attribs.get(PACKAGE).getValue(), attribs.get(NAME) + ".png")));
        }
    },
    CSS {
        @Override
        protected GameAttr[] getAutoAddDependencies() {
            return new GameAttr[] {NAME, PACKAGE};
        }
        
        @Override
        @SuppressWarnings("ConstantConditions")
        protected void autoAdd(Map<GameAttr, PossiblyTypedValue<?>> attribs) {
            attribs.put(this, new PossiblyTypedValue<>(new Resource(
                    (String) attribs.get(PACKAGE).getValue(), attribs.get(NAME) + ".css")));
        }
    },
    PLAYERS,
    AI,
    BOARD_FACTORY,
    PLACING_PIECES(new PossiblyTypedValue<>(new Player[0])),
    /** Override this to return false if your game has both placing and non-placing pieces */
    IS_PLACE_ONLY(new PossiblyTypedValue<>(false)) {
        @Override
        protected GameAttr[] getAutoAddDependencies() {
            return new GameAttr[] {PLACING_PIECES};
        }
        
        @Override
        @SuppressWarnings("ConstantConditions")
        protected void autoAdd(Map<GameAttr, PossiblyTypedValue<?>> attribs) {
            attribs.put(this, new PossiblyTypedValue<>(
                    ((PlacingPiece[]) attribs.get(PLACING_PIECES).getValue()).length > 0));
        }
    },
    ARBITRATOR,
    NOTATER,
    DEFAULTS_REGISTRATOR(new PossiblyTypedValue<>(new TypeLiteral<Consumer<EventBus>>() {}, b -> {})),
    ALLOW_PASSING(new PossiblyTypedValue<>(false)),
    AUTO_PASS(new PossiblyTypedValue<>(false));
    
    @Nullable private PossiblyTypedValue<?> defaultValue = null;
    
    // null = does not auto-add
    @Nullable
    protected GameAttr[] getAutoAddDependencies() {
        return null;
    }
    
    protected void autoAdd(Map<GameAttr, PossiblyTypedValue<?>> attribs) {}
    
    static void autoAddAllPossible(Map<GameAttr, PossiblyTypedValue<?>> attribs) {
        for (GameAttr attrib : values()) {
            if (attribs.containsKey(attrib)) continue; // no duplicates
            
            GameAttr[] deps = attrib.getAutoAddDependencies();
            if (deps == null) continue; // null = not an auto adder
            
            // check that all the dependencies are present
            boolean allDepsPresent = true;
            for (GameAttr dep : deps) {
                if (!attribs.containsKey(dep)) {
                    allDepsPresent = false;
                    break;
                }
            }
            
            if (allDepsPresent) {
                attrib.autoAdd(attribs);
            }
        }
    }
    
    /** Call after attribs has been filled out */
    static void fillInDefaults(Map<GameAttr, PossiblyTypedValue<?>> attribs) {
        for (GameAttr attrib : values()) {
            if (attrib.defaultValue != null && !attribs.containsKey(attrib)) {
                attribs.put(attrib, attrib.defaultValue);
            }
        }
    }
    
    public static GameAttribute attribute(GameAttr value) {
        return new GameAttributeImpl(value);
    }
    
}