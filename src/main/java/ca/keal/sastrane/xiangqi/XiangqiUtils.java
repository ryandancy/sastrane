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

import ca.keal.sastrane.api.GameUtils;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.move.MovingMove;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.util.TriFunction;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

final class XiangqiUtils {
    
    static final int MAXX = 8;
    static final int MAXY = 9;
    
    /** river is between ranks 4 and 5 */
    private static final int RIVER_Y = 4;
    
    private XiangqiUtils() {
        throw new IllegalStateException("XiangqiUtils is a utility class and therefore cannot be constructed.");
    }
    
    static boolean doesMoveCrossRiver(MovingMove move) {
        return (move.getFrom().getY() > RIVER_Y) != (move.getTo().getY() > RIVER_Y);
    }
    
    // The below is copy-pasted from KingInCheckUtils: merge into common class somehow???
    
    static Predicate<Move> checkGeneral(Round round, Player player) {
        return move -> !isGeneralInCheck(round.copyWithMove(move), player);
    }
    
    static List<Move> getPossibleMoves(TriFunction<Round, Square, Player, List<Move>> possibleMovesFunc,
                                              Round round, Square boardPos, Player player) {
        return possibleMovesFunc.apply(round, boardPos, player).stream()
                .filter(checkGeneral(round, player))
                .collect(Collectors.toList());
    }
    
    static boolean isGeneralInCheck(Round round, Player player) {
        // Find general, check if in check
        for (Square square : round.getBoard()) {
            OwnedPiece atSquare = round.getBoard().get(square);
            if (atSquare != null && atSquare.getOwner() == player && atSquare.getPiece() instanceof General) {
                return GameUtils.canBeMovedTo(round, square, player);
            }
        }
        return false;
    }
    
}