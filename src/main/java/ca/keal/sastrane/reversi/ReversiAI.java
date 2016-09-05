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

package ca.keal.sastrane.reversi;

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.GameUtils;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import com.google.common.collect.Multiset;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@ToString
class ReversiAI extends AI {
    
    ReversiAI(double difficulty) {
        super(difficulty);
    }
    
    @Override
    @SneakyThrows
    protected double heuristic(Round round, Set<Player> players) {
        // # of own disks - # of others' disks
        // Is there a better heuristic possible???
        if (players.size() != 1) throw new IllegalArgumentException("ReversiAI.heuristic: players.size() != 1");
        Player player = players.toArray(new Player[1])[0];
        
        Multiset<Player> counts = GameUtils.countPlayers(round.getBoard());
        return 2 * counts.count(player) - counts.size(); // equivalent to count(player) - (size() - count(player))
    }
    
}