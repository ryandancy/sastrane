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

package ca.keal.sastrane.util;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.piece.MovingPiece;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.Piece;
import ca.keal.sastrane.api.piece.RecursiveMovingPiece;
import com.google.common.collect.HashMultiset;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Utils {
    
    public static final Random RANDOM = new Random();
    
    private Utils() {}
    
    @SafeVarargs
    public static <T> T randomChoice(T... array) {
        return array[RANDOM.nextInt(array.length)];
    }
    
    /**
     * Returns whether the two collections have equal elements; that is, the same number of elements, and all elements
     * have an equal counterpart on the other set. Disregards order.
     */
    public static <E> boolean areElementsEqual(Collection<E> a, Collection<E> b) {
        return HashMultiset.create(a).equals(HashMultiset.create(b));
    }
    
    public static List<Move> perspectivizeAll(List<? extends Move> moves, Player player) {
        return perspectivize(moves.stream(), player).collect(Collectors.toList());
    }
    
    public static Stream<Move> perspectivize(Stream<? extends Move> moves, Player player) {
        return moves.map(move -> move.perspectivize(player));
    }
    
    /**
     * Returns whether any piece on {@code board} belonging to {@code player} (or not) can be moved to {@code square}.
     * {@code player == null} means any player is acceptable. If invert, then any player <i>not</i> the specified
     * player's pieces will be matched; else, <i>only</i> the specified player's pieces will be matched.
     */
    public static boolean canBeMovedTo(Round round, Square square, @Nullable Player player, boolean invert) {
        for (Square pos : round.getBoard()) {
            OwnedPiece posData = round.getBoard().get(pos);
            if (posData != null) {
                Piece piece = posData.getPiece();
                Player piecePlayer = posData.getOwner();
                
                if (piece instanceof MovingPiece && (player == null || (invert ? player != piecePlayer
                        : player == piecePlayer))) {
                    List<Move> moves;
                    if (piece instanceof RecursiveMovingPiece) {
                        moves = ((RecursiveMovingPiece) piece).getPossibleMovesNonRecursive(round, pos, piecePlayer);
                    } else {
                        moves = ((MovingPiece) piece).getPossibleMoves(round, pos, piecePlayer);
                    }
                    
                    if (moves.stream().map(Move::getEndPos).anyMatch(square::equals)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean canBeMovedTo(Round round, Square square, Player player) {
        return canBeMovedTo(round, square, player, true);
    }
    
    public static boolean canBeMovedTo(Round round, Square square) {
        return canBeMovedTo(round, square, null, true);
    }
    
}