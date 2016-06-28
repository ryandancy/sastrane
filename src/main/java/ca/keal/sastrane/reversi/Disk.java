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
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.PlacingMove;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.PlacingPiece;
import ca.keal.sastrane.util.Resource;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

class Disk extends PlacingPiece {
    
    @Nullable
    @Override
    public PlacingMove getMoveAt(Square square, Round round, Player player) {
        if (round.getBoard().get(square) != null) return null;
        
        List<Square> originals = getOriginalsAt(square, round.getBoard(), player);
        if (originals.size() != 0) {
            return new ReversiMove(player, originals, square);
        }
        
        return null;
    }
    
    private List<Square> getOriginalsAt(Square square, Board board, Player player) {
        List<Square> originals = new ArrayList<>();
        
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx == 0 && dy == 0) continue;
                
                Square xy = new Square(square.getX() + dx, square.getY() + dy);
                if (!board.isOn(xy)) continue;
                
                OwnedPiece atXy = board.get(xy);
                boolean squareBetween = false;
                while (atXy != null && atXy.getOwner() != player) {
                    xy = new Square(xy.getX() + dx, xy.getY() + dy);
                    try {
                        atXy = board.get(xy);
                    } catch (NoSuchElementException e) {
                        break;
                    }
                    squareBetween = true;
                }
                
                if (atXy != null && atXy.getOwner() == player && squareBetween) {
                    originals.add(xy);
                }
            }
        }
        
        return originals;
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.reversi", "disk.png");
    }
    
}