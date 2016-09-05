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

import ca.keal.sastrane.util.I18n;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Delegate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Singleton
@EqualsAndHashCode
@ToString
public class GameRegistrar {
    
    private interface GameCollection extends Collection<Game> {} // for the generics
    private interface Exceptions {
        boolean add(Game game);
    }
    
    @Delegate(types = GameCollection.class, excludes = Exceptions.class)
    private final Set<Game> impl = new HashSet<>();
    
    private final I18n i18n;
    
    public boolean add(Game game) {
        i18n.load(game.getResourceBundleName());
        return impl.add(game);
    }
    
    public void register(Game game) {
        add(game);
    }
    
}