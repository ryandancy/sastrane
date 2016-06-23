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

package ca.keal.sastrane.api.piece;

import ca.keal.sastrane.api.GameUtils;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.Move;

import java.util.List;

/**
 * A subinterface of Piece that represents a piece that resolves its possible moves recursively, generally using {@link
 * GameUtils#canBeMovedTo(Round, Square)}.
 */
public interface RecursiveMovingPiece extends MovingPiece {
    
    /**
     * Returns all possible moves this piece could make, without using recursion/{@link GameUtils#canBeMovedTo(Round, Square)}.
     */
    List<Move> getPossibleMovesNonRecursive(Round round, Square boardPos, Player player);
    
}