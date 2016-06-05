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
import ca.keal.sastrane.api.piece.RecursiveMovingPiece;
import ca.keal.sastrane.util.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Elephant implements RecursiveMovingPiece {
    
    @Override
    public List<Move> getPossibleMoves(Round round, Square boardPos, Player player) {
        return XiangqiUtils.getPossibleMoves(this::getPossibleMovesNonRecursive, round, boardPos, player);
    }
    
    @Override
    public List<Move> getPossibleMovesNonRecursive(Round round, Square boardPos, Player player) {
        List<MovingMove> moves = new ArrayList<>();
        
        // loop over all possible deltas
        for (int dx = -1; dx <= 1; dx += 2) {
            next: for (int dy = -1; dy <= 1; dy += 2) {
                // skip if either (x+dx, y+dy) or (x+dx*2, y+dy*2) aren't clear
                Square square = null;
                for (int i = 1; i <= 2; i++) {
                    square = new Square(boardPos.getX() + dx * i, boardPos.getY() + dy * i);
                    if (!round.getBoard().isOn(square) || round.getBoard().get(square) != null) {
                        continue next;
                    }
                }
                
                moves.add(boardPos.to(square));
            }
        }
        
        return moves.stream()
                .filter(move -> !XiangqiUtils.doesMoveCrossRiver(move))
                .collect(Collectors.toList());
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.xiangqi.piece", "elephant.png");
    }
    
}