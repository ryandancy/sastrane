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
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.util.TriFunction;
import ca.keal.sastrane.util.Utils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class KingInCheckUtils {
    
    private KingInCheckUtils() {}
    
    public static Predicate<Move> checkKing(Round round, Player player) {
        return move -> !isKingInCheck(round.copyWithMove(move), player);
    }
    
    public static List<Move> getPossibleMoves(TriFunction<Round, Square, Player, List<Move>> possibleMovesFunc,
                                              Round round, Square boardPos, Player player) {
        return possibleMovesFunc.apply(round, boardPos, player).stream()
                .filter(checkKing(round, player))
                .collect(Collectors.toList());
    }
    
    public static boolean isKingInCheck(Round round, Player player) {
        // Find king, check if in check
        for (Square square : round.getBoard()) {
            OwnedPiece atSquare = round.getBoard().get(square);
            if (atSquare != null && atSquare.getOwner() == player && atSquare.getPiece() instanceof King) {
                return Utils.canBeMovedTo(round, square, player);
            }
        }
        return false;
    }
    
}