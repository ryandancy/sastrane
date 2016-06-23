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

package ca.keal.sastrane.reversi;

import ca.keal.sastrane.api.Arbitrator;
import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.GameUtils;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Result;
import ca.keal.sastrane.api.Round;
import com.google.common.collect.Multiset;

class ReversiArbitrator implements Arbitrator {
    
    @Override
    public Result arbitrate(Round round) {
        Player player = ReversiPlayer.BLACK;
        Player opponent = ReversiPlayer.WHITE;
        
        for (int i = 0; i < 2; i++) {
            if (round.getAllPossibleMoves(player).size() == 0) {
                return count(round.getBoard(), player, opponent);
            }
        
            Player temp = player;
            player = opponent;
            opponent = temp;
        }
    
        return Result.NOT_OVER;
    }
    
    private Result count(Board board, Player player, Player opponent) {
        Multiset<Player> players = GameUtils.countPlayers(board);
        switch (Integer.compare(players.count(player), players.count(opponent))) {
            case -1:
                return new Result.Win(opponent);
            case 0:
                return Result.DRAW;
            case 1:
                return new Result.Win(player);
            default:
                // Wait what? This should NEVER happen...
                throw new RuntimeException("Why am I here? It's dark... and I'm scared... help...");
        }
    }
    
}