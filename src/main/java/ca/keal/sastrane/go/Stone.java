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
import ca.keal.sastrane.api.GameUtils;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.StateChange;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.move.PlacingMove;
import ca.keal.sastrane.api.piece.PlacingPiece;
import ca.keal.sastrane.util.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
class Stone implements PlacingPiece {
    
    private Square square;
    private Player player;
    
    /**
     * Add a {@link GoMove} at a point if it isn't occupied and the move isn't suicide or forbidden by the superko rule.
     *
     * @see #isSuicidal(Move, Board, Player)
     * @see #doesKoProhibit(Move, Round)
     */
    @Override
    public List<PlacingMove> getPossiblePlacements(Round round, Player player) {
        List<PlacingMove> moves = new ArrayList<>();
        assert player instanceof GoPlayer;
        
        for (Square square : round.getBoard()) {
            if (round.getBoard().get(square) == null) {
                GoMove move = new GoMove((GoPlayer) player, square);
                if (!isSuicidal(move, round.getBoard(), player) && !doesKoProhibit(move, round)) {
                    moves.add(move);
                }
            }
        }
        
        return moves;
    }
    
    /**
     * Returns whether a move is <a href="http://senseis.xmp.net/?Suicide">suicide</a>, defined as one's own stone(s)
     * being taken as a result of one's move. Suicide is forbidden under Chinese rules.
     */
    private boolean isSuicidal(Move move, Board board, Player player) {
        // The move is suicide if there are more or the same number of player's stones before than after
        return GameUtils.countPlayers(board).count(player) >= GameUtils.countPlayers(move.whatIf(board)).count(player);
    }
    
    /**
     * Returns whether <a href="http://senseis.xmp.net/?PositionalSuperko">positional superko</a>, used in the Chinese
     * rules, prohibits playing at {@code pos}. The positional superko rule (PSK) states that no previous board position
     * may <i>ever</i> occur a second time, preventing long-lasting cycles that prevent the game from coming to an end.
     */
    private boolean doesKoProhibit(Move move, Round round) {
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