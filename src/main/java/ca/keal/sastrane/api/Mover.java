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

import ca.keal.sastrane.api.move.Move;

/**
 * An object that returns the result of a player's move, whether that be from an AI, user input, or something else
 * entirely. If you're looking for a general description of a side, go to {@link Player}.
 *
 * @see Player
 */
public interface Mover {
    
    Move getMove(Round round, Player player);
    
    Decision decide(Decision[] options, Round round, Player player);
    
}