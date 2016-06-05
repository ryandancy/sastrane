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
import ca.keal.sastrane.api.move.MovingMove;
import ca.keal.sastrane.api.piece.JumpingPiece;
import ca.keal.sastrane.api.piece.RecursiveMovingPiece;
import ca.keal.sastrane.util.Resource;

import java.util.List;
import java.util.stream.Collectors;

/** Similar to chess' knight, except can't move through a piece **/
public class Horse extends JumpingPiece implements RecursiveMovingPiece {
    
    public Horse() {
        super(2, 1);
    }
    
    @Override
    public List<Move> getPossibleMoves(Round round, Square boardPos, Player player) {
        return XiangqiUtils.getPossibleMoves(this::getPossibleMovesNonRecursive, round, boardPos, player);
    }
    
    @Override
    public List<Move> getPossibleMovesNonRecursive(Round round, Square boardPos, Player player) {
        return super.getPossibleMoves(round, boardPos, player).stream().filter(move -> {
            assert move instanceof MovingMove : "uhhh... JumpingPiece.getPossibleMoves() returned something that's not"
                    + " a MovingMove...";
            MovingMove moving = (MovingMove) move;
            
            Square from = moving.getFrom();
            Square to = moving.getTo();
            int dx = to.getX() - from.getX();
            int dy = to.getY() - from.getY();
            int dxSignum = (int) Math.signum(dx);
            int dySignum = (int) Math.signum(dy);
            Square mustBeClear = new Square(to.getX() - dxSignum, to.getY() - dySignum);
            
            return round.getBoard().isOn(mustBeClear) && round.getBoard().get(mustBeClear) == null;
        }).collect(Collectors.toList());
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.xiangqi.piece", "horse.png");
    }
    
}