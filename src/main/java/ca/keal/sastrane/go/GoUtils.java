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
import ca.keal.sastrane.api.Square;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class GoUtils {
    
    /**
     * Return a list of all squares <a href="http://senseis.xmp.net/?Adjacent">adjacent</a> to this stone. Adjacency is
     * only horizontal and vertical; there is no diagonal adjacency in go.
     */
    static List<Square> getAdjacent(Square square, Board board) {
        List<Square> adjacents = new ArrayList<>();
        for (int delta = -1; delta <= 1; delta += 2) {
            addIfOnBoard(adjacents, square.withX(square.getX() + delta), board);
            addIfOnBoard(adjacents, square.withY(square.getY() + delta), board);
        }
        return adjacents;
    }
    
    /** Adds {@code square} to {@code squares} if it's on {@code board}. */
    private static void addIfOnBoard(List<Square> squares, Square square, Board board) {
        if (board.isOn(square)) {
            squares.add(square);
        }
    }
    
}