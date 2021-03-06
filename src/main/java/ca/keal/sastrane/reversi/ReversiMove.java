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
import ca.keal.sastrane.api.move.PlacingMove;
import ca.keal.sastrane.api.piece.OwnedPiece;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode(callSuper = true)
class ReversiMove extends PlacingMove {
    
    private final List<Square> originals;
    private final Player player;
    
    ReversiMove(Player player, List<Square> originals, Square pos) {
        super(new OwnedPiece(new Disk(), player), pos);
        this.originals = originals;
        this.player = player;
    }
    
    @Override
    public void move(Board board) {
        super.move(board);
        
        for (Square original : originals) {
            Square pos = getPos();
            int dx = (int) Math.signum(pos.getX() - original.getX());
            int dy = (int) Math.signum(pos.getY() - original.getY());
    
            for (int x = original.getX() + dx, y = original.getY() + dy;
                 !(x == pos.getX() && y == pos.getY());
                 x += dx, y += dy) {
                Square xy = new Square(x, y);
                OwnedPiece atXy = board.get(xy);
        
                // if atXy == null, we done messed up
                assert atXy != null && atXy.getOwner() != player : "YOU DONE MESSED UP BIG TIME in ReversiMove.move";
        
                board.set(xy, atXy.withOwner(player));
            }
        }
    }
    
}