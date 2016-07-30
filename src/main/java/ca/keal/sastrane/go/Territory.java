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

package ca.keal.sastrane.go;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.piece.OwnedPiece;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a <a href="http://senseis.xmp.net/?Territory">territory</a> of a player. A <i>territory</i> is defined as
 * a continguous block of empty space controlled by a player. A territory is <i>controlled</i> by a player if there is
 * no way to draw a path from it to a stone of the opposite colour.
 */
// This is VERY similar to Chain...combine???
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class Territory {
    
    private final List<Square> points;
    
    /** Null player -> neutral territory */
    @Nullable private final Player player;
    
    boolean isEmpty() {
        return points.isEmpty();
    }
    
    /**
     * Get a list of all of {@code player}'s territories anywhere on the board.
     */
    static List<Territory> getAllOfPlayer(Player player, Board board) {
        List<Territory> terrs = new ArrayList<>(); // all territories, not just player's
        
        for (Square square : board) {
            if (board.get(square) != null) continue; // we only want empty points
            
            if (terrs.stream().anyMatch(t -> t.getPoints().contains(square))) {
                // It's a duplicate territory
                continue;
            }
            
            terrs.add(at(square, board));
        }
        
        return terrs.stream()
                .filter(t -> t.getPlayer() == player)
                .collect(Collectors.toList());
    }
    
    /**
     * Attempt to find a territory containing {@code square} on {@code board}, and return an empty territory if
     * {@code square} is occupied.
     */
    static Territory at(Square square, Board board) {
        OwnedPiece piece = board.get(square);
        // Return null if piece != null for better performance???
        if (piece != null) return new Territory(new ArrayList<>(), null);
        
        boolean[][] points = new boolean[board.getMaxX()][board.getMaxY()];
        Player player = getPlayerAndAddPointsInTerritory(points, square, board);
        
        List<Square> squares = new ArrayList<>();
        
        for (int x = 0; x < points.length; x++) {
            for (int y = 0; y < points[x].length; y++) {
                if (points[x][y]) {
                    squares.add(new Square(x, y));
                }
            }
        }
        
        return new Territory(squares, player);
    }
    
    @Nullable
    private static Player getPlayerAndAddPointsInTerritory(boolean[][] points, Square start, Board board) {
        Player player = null;
        boolean playerInit = false;
        points[start.getX()][start.getY()] = true;
        
        Deque<Square> toCheck = new ArrayDeque<>(GoUtils.getAdjacent(start, board));
        while (!toCheck.isEmpty()) {
            Square adj = toCheck.removeLast();
            points[start.getX()][start.getY()] = true;
            
            OwnedPiece piece = board.get(adj);
            if (piece != null) {
                // Not an empty point
                if (!playerInit) {
                    player = piece.getOwner();
                    playerInit = true;
                } else if (piece.getOwner() != player) {
                    player = null;
                }
                continue;
            }
    
            GoUtils.getAdjacent(adj, board).stream()
                    .filter(sq -> !points[sq.getX()][sq.getY()])
                    .forEach(toCheck::push);
        }
        
        return player;
    }
    
}