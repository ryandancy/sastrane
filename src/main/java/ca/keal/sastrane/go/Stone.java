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

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.StateChange;
import ca.keal.sastrane.api.move.PlacingMove;
import ca.keal.sastrane.api.piece.PlacingPiece;
import ca.keal.sastrane.util.Resource;

import java.util.ArrayList;
import java.util.List;

class Stone implements PlacingPiece {
    
    /**
     * Add a {@link GoMove} at a point if it isn't occupied and the position isn't prohibited by the superko rule.
     * @see #doesKoProhibit(Round, GoMove)
     */
    @Override
    public List<PlacingMove> getPossiblePlacements(Round round, Player player) {
        List<PlacingMove> moves = new ArrayList<>();
        
        for (Square square : round.getBoard()) {
            if (round.getBoard().get(square) == null) {
                GoMove move = new GoMove(player, square);
                if (!doesKoProhibit(round, move)) {
                    moves.add(move);
                }
            }
        }
        
        return moves;
    }
    
    /**
     * Returns whether <a href="http://senseis.xmp.net/?PositionalSuperko">positional superko</a>, used in the Chinese
     * rules, prohibits playing at {@code pos}. The positional superko rule (PSK) states that no previous board position
     * may <i>ever</i> occur a second time, preventing long-lasting cycles that prevent the game from coming to an end.
     */
    private boolean doesKoProhibit(Round round, GoMove move) {
        Round withMove = round.copyWithMove(move);
        Board newBoard = withMove.getBoard();
        for (StateChange change : withMove.getMoves()) {
            if (change.getBefore().equals(newBoard)) {
                // New board position repeats
                return true;
            }
        }
        // No repetition found
        return false;
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.go", "stone.png");
    }
    
}