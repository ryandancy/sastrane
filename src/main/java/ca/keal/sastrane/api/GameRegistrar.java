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
import lombok.experimental.Delegate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Singleton
public class GameRegistrar {
    
    private interface GameCollection extends Collection<String> {} // for the generics
    private interface Exceptions {
        boolean add(String gameID);
    }
    
    @Delegate(types = GameCollection.class, excludes = Exceptions.class)
    private final Set<String> impl = new HashSet<>();
    
    private final Map<String, String> resourceBundleNames;
    
    @Inject
    public GameRegistrar(@GameAttribute(GameAttr.RESOURCE_BUNDLE_NAME) Map<String, String> resourceBundleNames) {
        this.resourceBundleNames = resourceBundleNames;
    }
    
    public boolean add(String gameID) {
        I18n.load(resourceBundleNames.get(gameID));
        return impl.add(gameID);
    }
    
    public void register(String gameID) {
        add(gameID);
    }
    
}