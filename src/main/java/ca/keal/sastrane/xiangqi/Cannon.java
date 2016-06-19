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

package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.piece.LinePiece;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.RecursiveMovingPiece;
import ca.keal.sastrane.util.Resource;

import java.util.List;

class Cannon extends LinePiece implements RecursiveMovingPiece {
    
    Cannon() {
        super(true, false, UP | DOWN | LEFT | RIGHT);
    }
    
    @Override
    public List<Move> getPossibleMoves(Round round, Square boardPos, Player player) {
        return XiangqiUtils.getPossibleMoves(this::getPossibleMovesNonRecursive, round, boardPos, player);
    }
    
    @Override
    public List<Move> getPossibleMovesNonRecursive(Round round, Square boardPos, Player player) {
        List<Move> moves = super.getPossibleMoves(round, boardPos, player);
    
        // Call addCapture in all 4 directions
        for (int i = -1; i <= 1; i += 2) {
            addCapture(moves, round, boardPos, player, i, 0);
            addCapture(moves, round, boardPos, player, 0, i);
        }
    
        return moves;
    }
    
    private void addCapture(List<Move> moves, Round round, Square boardPos, Player player, int dx, int dy) {
        // If possible, add a move to an enemy piece behind any piece (the screen)
        boolean foundScreen = false;
        for (int x = boardPos.getX() + dx, y = boardPos.getY() + dy;
             round.getBoard().isOn(new Square(x, y));
             x += dx, y += dy) {
            Square square = new Square(x, y);
            OwnedPiece atSquare = round.getBoard().get(square);
            if (atSquare == null) continue;
            if (foundScreen) {
                if (atSquare.getOwner() != player) {
                    moves.add(boardPos.to(square));
                }
                return;
            } else {
                foundScreen = true;
            }
        }
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.xiangqi.piece", "cannon.png");
    }
    
}