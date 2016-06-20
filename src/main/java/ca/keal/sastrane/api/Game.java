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
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class Game {
    
    private static final List<Game> GAMES = new ArrayList<>();
    
    private final GameInfo info;
    private EventBus bus;
    
    public Game(GameInfo info) {
        this.info = info;
        I18n.load(info.getResourceBundleName());
        bus = new EventBus(info.getResourceBundleName());
        info.registerDefaults(bus);
    }
    
    public static void registerGame(Game game) {
        GAMES.add(game);
    }
    
    public static List<Game> getGames() {
        return ImmutableList.copyOf(GAMES);
    }
    
    /**
     * Throws away the old bus and replaces it with a new one; has the effect of unregistering all subscribers.
     */
    public void refreshBus() {
        bus = new EventBus(info.getResourceBundleName());
        info.registerDefaults(bus);
    }
    
}