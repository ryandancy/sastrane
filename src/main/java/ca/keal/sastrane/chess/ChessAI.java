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

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.Piece;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@ToString
class ChessAI extends AI {
    
    private static final Map<Class<? extends Piece>, Double> pieceToNaiveValue = new HashMap<>();
    
    static {
        pieceToNaiveValue.put(Pawn.class, 1.0);
        pieceToNaiveValue.put(Knight.class, 3.0);
        pieceToNaiveValue.put(Bishop.class, 3.0);
        pieceToNaiveValue.put(Rook.class, 5.0);
        pieceToNaiveValue.put(Queen.class, 9.0);
        pieceToNaiveValue.put(King.class, 1e9); // not Double.MAX_VALUE to avoid overflow
    }
    
    ChessAI(double difficulty) {
        super(difficulty);
    }
    
    @Override
    protected double heuristic(Round round, Set<Player> players) {
        // Naive piece value method
        // TODO improve piece valuation over naive method
        if (players.size() != 1) throw new IllegalArgumentException("ChessAI.heuristic: players.size() != 1");
        Player player = players.toArray(new Player[1])[0];
        
        // value = sum of own pieces - sum of everyone else's pieces
        double sumOwnPieces = 0;
        double sumOpponentPieces = 0;
        for (Square square : round.getBoard()) {
            OwnedPiece atSquare = round.getBoard().get(square);
            if (atSquare == null) continue;
            double naiveValue = pieceToNaiveValue.get(atSquare.getPiece().getClass());
            if (atSquare.getOwner() == player) {
                sumOwnPieces += naiveValue;
            } else {
                sumOpponentPieces += naiveValue;
            }
        }
        
        return sumOwnPieces - sumOpponentPieces;
    }
    
}