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

package ca.keal.sastrane.api.move;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Square;

public interface Move {
    
    void move(Board board);
    
    Move perspectivize(Player player);
    
    /**
     * Returns the ending position of the move; that is, the position this move's affected piece will have. For example,
     * in chess, the {@code getEndPos} of the {@link Move} that describes the pawn's single space move forwards will
     * return the {@link Square} that represents the space in front of the pawn.
     */
    Square getEndPos();
    
}