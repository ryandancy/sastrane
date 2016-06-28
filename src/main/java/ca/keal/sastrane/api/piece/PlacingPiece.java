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

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.PlacingMove;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
public abstract class PlacingPiece implements Piece {
    
    /** Gets a list of all PlacingMoves this piece could possibly execute (i.e. be placed at). */
    public List<PlacingMove> getPossiblePlacements(Round round, Player player) {
        List<PlacingMove> moves = new ArrayList<>();
        for (Square square : round.getBoard()) {
            PlacingMove move = getMoveAt(square, round, player);
            if (move != null) {
                moves.add(move);
            }
        }
        return moves;
    }
    
    /** returns null -> can't place at square. */
    @Nullable
    public abstract PlacingMove getMoveAt(Square square, Round round, Player player);
    
}
