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

package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.Arbitrator;
import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.Notatable;
import ca.keal.sastrane.api.Notater;
import ca.keal.sastrane.api.Player;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class XiangqiGame implements Game, Notatable {
    
    static final int MAXX = 9;
    static final int MAXY = 10;
    
    @Getter(AccessLevel.NONE) private final PalaceLines palaceLines;
    
    private final Game.Name name;
    private final Game.Package packageName;
    private final Player[] players;
    private final Function<Double, AI> aI;
    private final Board.Factory boardFactory;
    private final Arbitrator arbitrator;
    private final Notater notater;
    @Getter(AccessLevel.NONE) private final Void __; // so as not to cause constructor conflicts with Lombok
    
    @Inject
    public XiangqiGame(PalaceLines palaceLines, @Xiangqi Game.Name name, @Xiangqi Game.Package packageName,
                       @Xiangqi Player[] players, @Xiangqi Function<Double, AI> ai, @Xiangqi Board.Factory boardFactory,
                       @Xiangqi Arbitrator arbitrator, @Xiangqi Notater notater) {
        this(palaceLines, name, packageName, players, ai, boardFactory, arbitrator, notater, null);
    }
    
    @Override
    public void registerDefaults(EventBus bus) {
        bus.register(palaceLines);
    }
    
}