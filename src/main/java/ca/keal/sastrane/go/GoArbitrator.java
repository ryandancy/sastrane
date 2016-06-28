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
import ca.keal.sastrane.api.Result;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.StateChange;
import ca.keal.sastrane.api.move.Move;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@ToString
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
        double scoreBlack = GoUtils.getScore(GoPlayer.BLACK, komi, board);
        double scoreWhite = GoUtils.getScore(GoPlayer.WHITE, komi, board);
        
        if (scoreBlack == scoreWhite) {
            // Under Chinese rules this shouldn't happen, because komi is fractional, but whatever
            return Result.DRAW;
        }
        
        return new Result.Win(scoreBlack > scoreWhite ? GoPlayer.BLACK : GoPlayer.WHITE);
    }
    
}