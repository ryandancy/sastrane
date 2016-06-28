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

package ca.keal.sastrane.chess;

import ca.keal.sastrane.api.Arbitrator;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Result;
import ca.keal.sastrane.api.Round;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
class ChessArbitrator implements Arbitrator {
    
    @Override
    public Result arbitrate(Round round) {
        Player player = ChessPlayer.WHITE;
        Player opponent = ChessPlayer.BLACK;
        
        for (int i = 0; i < 2; i++) {
            if (round.getAllPossibleMoves(player).size() == 0) {
                if (KingInCheckUtils.isKingInCheck(round, player)) {
                    return new Result.Win(opponent);
                } else {
                    return Result.DRAW;
                }
            }
        
            Player temp = opponent;
            opponent = player;
            player = temp;
        }
        
        return Result.NOT_OVER;
    }
    
}