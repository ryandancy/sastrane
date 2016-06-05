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

package ca.keal.sastrane.api.piece;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.Move;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A piece that moves in a straight line (possibly diagonally), possibly stopping when it hits a piece and possibly
 * taking an opponent's piece.
 * <p>
 * This class provides both an instance and a static implementation of {@code getPossibleMoves}; the static version is
 * only provided such that this class can be used with another piece utility-type class, as Java does not support
 * multiple inheritance.
 */
@AllArgsConstructor
public abstract class LinePiece implements MovingPiece {
    
    private static final Map<Integer, Offset> directionToOffset = new HashMap<>();
    
    /** +y */
    public static final int UP = 0x01;
    /** +x, +y */
    public static final int UP_RIGHT = 0x02;
    /** +x */
    public static final int RIGHT = 0x04;
    /** +x, -y */
    public static final int DOWN_RIGHT = 0x08;
    /** -y */
    public static final int DOWN = 0x10;
    /** -x, -y */
    public static final int DOWN_LEFT = 0x20;
    /** -x */
    public static final int LEFT = 0x40;
    /** -x, +y */
    public static final int UP_LEFT = 0x80;
    
    static {
        directionToOffset.put(UP, new Offset(0, +1));
        directionToOffset.put(UP_RIGHT, new Offset(+1, +1));
        directionToOffset.put(RIGHT, new Offset(+1, 0));
        directionToOffset.put(DOWN_RIGHT, new Offset(+1, -1));
        directionToOffset.put(DOWN, new Offset(0, -1));
        directionToOffset.put(DOWN_LEFT, new Offset(-1, -1));
        directionToOffset.put(LEFT, new Offset(-1, 0));
        directionToOffset.put(UP_LEFT, new Offset(-1, +1));
    }
    
    private final boolean stopOnHitPiece;
    private final boolean takeOpposingPieces;
    
    /**
     * Bitmap of directions lines will go in.
     */
    private final int directions;
    
    public LinePiece(boolean stopOnHitPiece, int directions) {
        this(stopOnHitPiece, true, directions);
    }
    
    public LinePiece(int directions) {
        this(true, directions);
    }
    
    @Override
    public List<Move> getPossibleMoves(Round round, Square boardPos, Player player) {
        return getPossibleMoves(round, boardPos, player, stopOnHitPiece, takeOpposingPieces, directions);
    }
    
    public static List<Move> getPossibleMoves(Round round, Square boardPos,
                                              Player player, boolean stopOnHitPiece,
                                              boolean takeOpposingPieces, int directions) {
        List<Move> res = new ArrayList<>();
        for (int i = 0; i < directionToOffset.size(); i++) {
            if ((directions & (1 << i)) != 0) {
                res.addAll(getMovesInLine(directionToOffset.get(1 << i), round, boardPos, player, stopOnHitPiece,
                        takeOpposingPieces));
            }
        }
        return res;
    }
    
    private static List<Move> getMovesInLine(Offset offset, Round round, Square boardPos, Player player,
                                             boolean stopOnHitPiece, boolean takeOpposingPieces) {
        int dx = offset.getDx();
        int dy = offset.getDy();
        List<Move> res = new ArrayList<>();
        
        for (int x = boardPos.getX() + dx, y = boardPos.getY() + dy;
             round.getBoard().isOn(player.perspectivize(new Square(x, y), boardPos));
             x += dx, y += dy) {
            Square square = player.perspectivize(new Square(x, y), boardPos);
            OwnedPiece atSquare = round.getBoard().get(square);
            if (atSquare == null) {
                res.add(boardPos.to(square));
            } else {
                if (player != atSquare.getOwner() && takeOpposingPieces) {
                    res.add(boardPos.to(square));
                }
                if (stopOnHitPiece) break;
            }
        }
        
        return res;
    }
    
    public static List<Move> getPossibleMoves(Round round, Square boardPos, Player player, boolean stopOnHitPiece,
                                              int directions) {
        return getPossibleMoves(round, boardPos, player, stopOnHitPiece, true, directions);
    }
    
    public static List<Move> getPossibleMoves(Round round, Square boardPos, Player player, int directions) {
        return getPossibleMoves(round, boardPos, player, true, directions);
    }
    
}