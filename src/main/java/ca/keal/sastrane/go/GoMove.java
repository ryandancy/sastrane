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
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.PlacingMove;
import ca.keal.sastrane.api.piece.OwnedPiece;

class GoMove extends PlacingMove {
    
    private final GoPlayer player;
    
    GoMove(GoPlayer player, Square pos) {
        super(new OwnedPiece(new Stone(pos, player), player), pos);
        this.player = player;
    }
    
    /**
     * Follows the steps of moving described <a href="https://en.wikipedia.org/wiki/Rules_of_go#Moving">on
     * Wikipedia</a>. They are:
     * <ol>
     *     <li>Place the stone on the desired intersection.</li>
     *     <li>Remove any of the opponent's stones with no liberties.</li>
     *     <li>Remove any of one's own stones with no liberties.</li>
     * </ol>
     * Chinese rules prohibit suicide, which means that step #3 never occurs. However, the implementation of suicide
     * prohibition in {@link Stone} requires that step #3 be implemented here, and so it is.
     * 
     * @implNote Step #1 is handled by PlacingMove, which means that this method implements steps #2 and #3.
     */
    @Override
    public void move(Board board) {
        // Step #1 (placing the stone) is handled by PlacingMove
        super.move(board);
        
        // Step #2: Remove all opponent's stones with no liberties
        removeAllStonesWithoutLiberties(player.getOpponent(), board);
        
        // Step #3: Remove all own stones with no liberties
        removeAllStonesWithoutLiberties(player, board);
    }
    
    /**
     * Removes all of {@code player}'s stones without liberties. A <i>liberty</i> of a stone is an empty point adjacent
     * to that stone or a stone connected to it. Two like-coloured stones are said to be <i>connected</i> if a unbroken
     * line can be drawn between them through only like-coloured stones.
     */
    private void removeAllStonesWithoutLiberties(Player player, Board board) {
        Chain.getAllOfPlayer(player, board).stream()
                .filter(chain -> chain.getLiberties(board).isEmpty())
                .forEach(chain -> removeChain(chain, board));
    }
    
    /** Convenience method for removing whole chains from the board. */
    private void removeChain(Chain chain, Board board) {
        chain.getStones().forEach(s -> removeStone(s.getSquare(), board));
    }
    
    /** Convenience method for removing stones from the board. */
    private void removeStone(Square square, Board board) {
        board.set(square, null);
    }
    
}