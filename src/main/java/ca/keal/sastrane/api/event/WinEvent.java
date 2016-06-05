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

package ca.keal.sastrane.api.event;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Result;
import ca.keal.sastrane.api.Round;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;

/**
 * An event posted when a game is won. Game implementations should post this when the winning criteria for the game have
 * been met.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WinEvent extends RoundEvent {
    
    /**
     * The winner of the game. {@code null} means the game was a tie.
     */
    @Nullable private final Player winner;
    
    public WinEvent(Round round, @Nullable Player winner) {
        super(round);
        this.winner = winner;
    }
    
    public WinEvent(Round round, Result result) {
        this(round, result instanceof Result.Win ? ((Result.Win) result).getPlayer() : null);
    }
    
}