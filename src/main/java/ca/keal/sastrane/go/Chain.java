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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents an unbroken group of like-coloured stones.
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class Chain {
    
    @Getter private final List<Stone> stones;
    
    boolean isEmpty() {
        return stones.isEmpty();
    }
    
    Player getPlayer() {
        if (isEmpty()) throw new IllegalStateException("Empty chain has no player");
        return stones.get(0).getPlayer();
    }
    
    /**
     * Return all <a href="http://senseis.xmp.net/?LibertyIntroductory">liberties</a> of this chain.
     * @implNote Simply defers to {@link Stone#getLiberties(Board)}
     */
    // Return Set<Square> here and in Stone???
    List<Square> getLiberties(Board board) {
        return new ArrayList<>(stones.stream()
                .map(s -> s.getLiberties(board))
                .collect(HashSet::new, Set::addAll, Set::addAll));
    }
    
    /**
     * Get a list of all chains of {@code player}'s color anywhere on the board.
     */
    static List<Chain> getAllOfPlayer(Player player, Board board) {
        List<Chain> chains = new ArrayList<>();
        for (Square square : board) {
            OwnedPiece atSquare = board.get(square);
            if (atSquare == null || atSquare.getOwner() != player) {
                // It's not a stone of our colour
                continue;
            }
            
            Stone stone = (Stone) atSquare.getPiece();
            if (chains.stream().anyMatch(c -> c.getStones().contains(stone))) {
                // It's a duplicate chain
                continue;
            }
            
            Chain chain = at(square, board);
            if (!chain.isEmpty()) {
                chains.add(chain);
            }
        }
        return chains;
    }
    
    /**
     * Attempt to find a chain containing {@code square} on {@code board}, and returns an empty chain if {@code square}
     * is empty.
     */
    static Chain at(Square square, Board board) {
        OwnedPiece piece = board.get(square);
        // Return null if piece == null for better performance???
        if (piece == null) return new Chain(new ArrayList<>()); // square is empty
        
        Stone stone = (Stone) piece.getPiece();
        
        List<Stone> stones = new ArrayList<>();
        addStonesInChain(stones, stone, board);
        
        return new Chain(stones);
    }
    
    /** Recursively adds all stones part of the chain. */
    private static void addStonesInChain(List<Stone> stones, Stone start, Board board) {
        stones.add(start);
        for (Square adj : GoUtils.getAdjacent(start.getSquare(), board)) {
            OwnedPiece piece = board.get(adj);
            if (piece == null) continue; // empty point
            
            Stone stone = (Stone) piece.getPiece();
            if (stone.getPlayer() != start.getPlayer()) continue; // must be same colour
            if (stones.contains(stone)) continue; // don't call this method for the same stone twice
            
            addStonesInChain(stones, stone, board);
        }
    }
    
}