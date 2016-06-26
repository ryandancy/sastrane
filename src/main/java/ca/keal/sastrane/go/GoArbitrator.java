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

import ca.keal.sastrane.api.Arbitrator;
import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.GameUtils;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Result;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.StateChange;
import ca.keal.sastrane.api.move.Move;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.List;

class GoArbitrator implements Arbitrator {
    
    /** 
     * The <a href="http://senseis.xmp.net/?Komi">komi</a> given to White for scoring purposes. Under Chinese rules,
     * it's 7.5.
     */
    private final double komi;
    
    @Inject
    GoArbitrator(@Named("komi") double komi) {
        this.komi = komi;
    }
    
    @Override
    public Result arbitrate(Round round) {
        if (isOver(round)) {
            return getVictor(round.getBoard());
        }
        
        return Result.NOT_OVER;
    }
    
    /** Return whether the game is over; that is, the last two moves were passes */
    private boolean isOver(Round round) {
        List<StateChange> moves = round.getMoves();
        return moves.size() >= 2
                && moves.subList(moves.size() - 2, moves.size()).stream() // last two moves
                    .allMatch(m -> m.getMove() == Move.PASS);
    }
    
    private Result getVictor(Board board) {
        int scoreBlack = getScoreBeforeKomi(GoPlayer.BLACK, board);
        double scoreWhite = getScoreBeforeKomi(GoPlayer.WHITE, board) + komi;
        
        if (scoreBlack == scoreWhite) {
            // Under Chinese rules this shouldn't happen, because komi is fractional, but whatever
            return Result.DRAW;
        }
        
        return new Result.Win(scoreBlack > scoreWhite ? GoPlayer.BLACK : GoPlayer.WHITE);
    }
    
    /**
     * Get a player's score, before komi is factored in. Chinese rules use <a
     * href="http://senseis.xmp.net/?AreaScoring"><i>area scoring</i></a>, meaning that a player's score is the sum of
     * two components:
     * <ul>
     *     <li>
     *         the number of empty points surrounded by the player's stones (i.e. that player's
     *         <a href="http://senseis.xmp.net/?Territory">territory</a>), and
     *     </li>
     *     <li>the number of the player's stones on the board.</li>
     * </ul>
     */
    private int getScoreBeforeKomi(Player player, Board board) {
        // Number of player's stones on the board + player's territory
        return GameUtils.countPlayers(board).count(player)
            + Territory.getAllOfPlayer(player, board).stream()
                    .flatMap(t -> t.getPoints().stream()) // get all points of all territories
                    .mapToInt(p -> 1).sum();              // count them
    }
    
}