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

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.Arbitrator;
import ca.keal.sastrane.api.GameAttr;
import ca.keal.sastrane.api.GameAttribute;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.Piece;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// This class is *VERY* similar to ChessAI... consolidate???
@EqualsAndHashCode(callSuper = true)
@ToString
class XiangqiAI extends AI {
    
    private static final Map<Class<? extends Piece>, Double> pieceToNaiveValue = new HashMap<>();
    
    static {
        // Soldier is handled as a special case in getNaiveValue
        pieceToNaiveValue.put(Advisor.class, 2.0);
        pieceToNaiveValue.put(Elephant.class, 2.0);
        pieceToNaiveValue.put(Horse.class, 4.0);
        pieceToNaiveValue.put(Cannon.class, 4.5);
        pieceToNaiveValue.put(Chariot.class, 9.0);
        pieceToNaiveValue.put(General.class, 1e6); // not Double.MAX_VALUE to avoid overflow
    }
    
    @Inject
    XiangqiAI(@Assisted double difficulty, @GameAttribute(GameAttr.PLAYERS) Map<String, Player[]> players,
              @GameAttribute(GameAttr.ARBITRATOR) Map<String, Arbitrator> arbitrators,
              @GameAttribute(GameAttr.ALLOW_PASSING) Map<String, Boolean> canPass) {
        super(difficulty, players, arbitrators, canPass);
    }
    
    @Override
    protected double heuristic(Round round, Set<Player> players) {
        // Naive piece value method
        // TODO improve piece valuation over naive method
        if (players.size() != 1) throw new IllegalArgumentException("XiangqiAI.heuristic: players.size() != 1");
        Player player = players.toArray(new Player[1])[0];
        
        // value = sum of own pieces - sum of everyone else's pieces
        double sumOwnPieces = 0;
        double sumOpponentPieces = 0;
        for (Square square : round.getBoard()) {
            OwnedPiece atSquare = round.getBoard().get(square);
            if (atSquare != null) {
                double naiveValue = getNaiveValue(atSquare.getPiece());
                if (atSquare.getOwner() == player) {
                    sumOwnPieces += naiveValue;
                } else {
                    sumOpponentPieces += naiveValue;
                }
            }
        }
    
        return sumOwnPieces - sumOpponentPieces;
    }
    
    private double getNaiveValue(Piece piece) {
        if (piece instanceof Soldier) {
            return ((Soldier) piece).isAcrossRiver() ? 2.0 : 1.0;
        } else {
            return pieceToNaiveValue.get(piece.getClass());
        }
    }
    
}