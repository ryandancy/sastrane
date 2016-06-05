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

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.piece.OwnedPiece;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

final class ReversiUtils {
    
    public static Multiset<Player> countPlayers(Board board) {
        Multiset<Player> players = HashMultiset.create();
    
        for (Square square : board) {
            OwnedPiece atSquare = board.get(square);
            if (atSquare != null) {
                players.add(atSquare.getOwner());
            }
        }
        
        return players;
    }
    
}