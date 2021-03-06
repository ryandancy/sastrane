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

package ca.keal.sastrane.go;

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@ToString
class GoAI extends AI {
    
    private final double komi;
    
    @Inject
    public GoAI(@Assisted double difficulty, @Named("komi") double komi) {
        super(difficulty);
        this.komi = komi;
    }
    
    /** Simply caculates the players' score and sums it. */
    @Override
    protected double heuristic(Round round, Set<Player> players) {
        // Calculate score for each player, sum
        return players.stream()
                .mapToDouble(p -> GoUtils.getScore(p, komi, round.getBoard()))
                .sum();
    }
    
    @Override
    protected int getDepth(double difficulty) {
        // Go has so many possibilities that anything > 3 plies deep would take an incredible amount of time
        return (int) Math.round(2 * difficulty) + 1;
    }
    
}