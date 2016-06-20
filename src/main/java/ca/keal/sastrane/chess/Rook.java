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

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.piece.LinePiece;
import ca.keal.sastrane.api.piece.MoveCountingPiece;
import ca.keal.sastrane.api.piece.RecursiveMovingPiece;
import ca.keal.sastrane.util.Resource;
import lombok.Getter;

import java.util.List;

@Getter
class Rook extends LinePiece implements MoveCountingPiece, RecursiveMovingPiece {
    
    private int numMoves = 0;
    
    Rook(Round round) {
        super(UP | LEFT | DOWN | RIGHT);
        round.getBus().register(this);
    }
    
    @Override
    public List<Move> getPossibleMoves(Round round, Square boardPos, Player player) {
        return KingInCheckUtils.getPossibleMoves(super::getPossibleMoves, round, boardPos, player);
    }
    
    @Override
    public List<Move> getPossibleMovesNonRecursive(Round round, Square boardPos, Player player) {
        return super.getPossibleMoves(round, boardPos, player);
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.chess.piece", "rook.png");
    }
    
    @Override
    public void incrementMoveCount() {
        numMoves++;
    }
    
}