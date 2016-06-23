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
public enum GameAttrib {
    
    NAME,
    PACKAGE,
    RESOURCE_BUNDLE_NAME {
        @Override
        protected GameAttrib[] getAutoAddDependencies() {
            return new GameAttrib[] {NAME, PACKAGE};
        }
        
        @Override
        protected void autoAdd(Map<GameAttrib, PossiblyTypedValue<?>> attribs) {
            attribs.put(this, new PossiblyTypedValue<>(attribs.get(PACKAGE) + ".i18n." + attribs.get(NAME)));
        }
    },
    I18N_NAME {
        @Override
        protected GameAttrib[] getAutoAddDependencies() {
            return new GameAttrib[] {NAME};
        }
        
        @Override
        protected void autoAdd(Map<GameAttrib, PossiblyTypedValue<?>> attribs) {
            attribs.put(this, new PossiblyTypedValue<>(attribs.get(NAME) + ".name"));
        }
    },
    ICON {
        @Override
        protected GameAttrib[] getAutoAddDependencies() {
            return new GameAttrib[] {NAME, PACKAGE};
        }
        
        @Override
        @SuppressWarnings("ConstantConditions")
        protected void autoAdd(Map<GameAttrib, PossiblyTypedValue<?>> attribs) {
            attribs.put(this, new PossiblyTypedValue<>(new Resource(
                    (String) attribs.get(PACKAGE).getValue(), attribs.get(NAME) + ".png")));
        }
    },
    CSS {
        @Override
        protected GameAttrib[] getAutoAddDependencies() {
            return new GameAttrib[] {NAME, PACKAGE};
        }
        
        @Override
        @SuppressWarnings("ConstantConditions")
        protected void autoAdd(Map<GameAttrib, PossiblyTypedValue<?>> attribs) {
            assert attribs.get(PACKAGE).getValue() != null;
            attribs.put(this, new PossiblyTypedValue<>(new Resource(
                    (String) attribs.get(PACKAGE).getValue(), attribs.get(NAME).getValue() + ".css")));
        }
    },
    PLAYERS,
    AI,
    BOARD_FACTORY,
    PLACING_PIECES(new PossiblyTypedValue<>(new Player[0])),
    ARBITRATOR,
    NOTATER,
    DEFAULTS_REGISTRATOR(new PossiblyTypedValue<>(new TypeLiteral<Consumer<EventBus>>() {}, b -> {}));
    
    @Nullable private PossiblyTypedValue<?> defaultValue = null;
    
    // null = does not auto-add
    @Nullable
    protected GameAttrib[] getAutoAddDependencies() {
        return null;
    }
    
    protected void autoAdd(Map<GameAttrib, PossiblyTypedValue<?>> attribs) {}
    
    static void autoAddAllPossible(Map<GameAttrib, PossiblyTypedValue<?>> attribs) {
        for (GameAttrib attrib : values()) {
            if (attribs.containsKey(attrib)) continue; // no duplicates
            
            GameAttrib[] deps = attrib.getAutoAddDependencies();
            if (deps == null) continue; // null = not an auto adder
            
            // check that all the dependencies are present
            boolean allDepsPresent = true;
            for (GameAttrib dep : deps) {
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
    static void fillInDefaults(Map<GameAttrib, PossiblyTypedValue<?>> attribs) {
        for (GameAttrib attrib : values()) {
            if (attrib.defaultValue != null && !attribs.containsKey(attrib)) {
                attribs.put(attrib, attrib.defaultValue);
            }
        }
    }
    
    public static GameAttribute attribute(GameAttrib value) {
        return new GameAttributeImpl(value);
    }
    
}