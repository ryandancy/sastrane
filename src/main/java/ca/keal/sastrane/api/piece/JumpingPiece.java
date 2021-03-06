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
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode
@ToString
public abstract class JumpingPiece implements MovingPiece {
    
    private static final Map<Integer, Offset> quadrantsToSigns = new HashMap<>();
    
    // https://commons.wikimedia.org/wiki/File:Cartesian-coordinate-system-with-quadrant.svg
    /** Quadrant I: +x, +y */
    public static final int QI = 0x1;
    /** Quadrant II: -x, +y */
    public static final int QII = 0x2;
    /** Quadrant III: -x, -y */
    public static final int QIII = 0x4;
    /** Quadrant IV: +x, -y */
    public static final int QIV = 0x8;
    
    static {
        quadrantsToSigns.put(QI, new Offset(+1, +1));
        quadrantsToSigns.put(QII, new Offset(-1, +1));
        quadrantsToSigns.put(QIII, new Offset(-1, -1));
        quadrantsToSigns.put(QIV, new Offset(+1, -1));
    }
    
    private final List<Offset> offsets;
    private final boolean takeOpposingPieces;
    
    private final boolean mirror;
    private final int quadrants;
    
    public JumpingPiece(int xOffset, int yOffset, boolean mirror, boolean takeOpposingPieces, int quadrants) {
        this.takeOpposingPieces = takeOpposingPieces;
        this.mirror = mirror;
        this.quadrants = quadrants;
        offsets = calculateOffsets(xOffset, yOffset, mirror, quadrants);
    }
    
    public JumpingPiece(int xOffset, int yOffset, boolean mirror, int quadrants) {
        this(xOffset, yOffset, mirror, true, quadrants);
    }
    
    public JumpingPiece(int xOffset, int yOffset, int quadrants) {
        this(xOffset, yOffset, true, quadrants);
    }
    
    public JumpingPiece(int xOffset, int yOffset, boolean mirror) {
        this(xOffset, yOffset, mirror, QI | QII | QIII | QIV);
    }
    
    public JumpingPiece(int xOffset, int yOffset) {
        this(xOffset, yOffset, true);
    }
    
    public void addOffsets(int xOffset, int yOffset) {
        offsets.addAll(calculateOffsets(xOffset, yOffset, mirror, quadrants));
    }
    
    @Override
    public List<Move> getPossibleMoves(Round round, Square boardPos, Player player) {
        return getPossibleMoves(round, boardPos, player, takeOpposingPieces, offsets);
    }
    
    @SuppressWarnings("SuspiciousNameCombination")
    private static List<Offset> calculateOffsets(int xOffset, int yOffset, boolean mirror, int quadrants) {
        List<Offset> offsets = new ArrayList<>();
        
        // The ^= stuff swaps x and y
        for (int x = xOffset, y = yOffset, i = 0; i < (mirror ? 2 : 1); x ^= y, y ^= x, x ^= y, i++) {
            for (int j = 0; j < quadrantsToSigns.size(); j++) {
                if ((quadrants & (1 << j)) != 0) {
                    Offset signs = quadrantsToSigns.get(1 << j);
                    offsets.add(new Offset(signs.getDx() * x, signs.getDy() * y));
                }
            }
        }
        
        return offsets;
    }
    
    public static List<Move> getPossibleMoves(Round round, Square boardPos, Player player, boolean takeOpposingPieces,
                                              List<Offset> offsets) {
        List<Move> res = new ArrayList<>();
        for (Offset offset : offsets) {
            Square deltaPos = player.perspectivize(new Square(boardPos.getX() + offset.getDx(),
                    boardPos.getY() + offset.getDy()), boardPos);
            if (!round.getBoard().isOn(deltaPos)) continue;
            
            OwnedPiece atDelta = round.getBoard().get(deltaPos);
            if (atDelta == null || (player != atDelta.getOwner() && takeOpposingPieces)) {
                res.add(boardPos.to(deltaPos));
            }
        }
        return res;
    }
    
    public static List<Move> getPossibleMoves(Round round, Square boardPos, Player player, List<Offset> offsets) {
        return getPossibleMoves(round, boardPos, player, true, offsets);
    }
    
    public static List<Move> getPossibleMoves(Round round, Square boardPos,
                                              Player player, int xOffset, int yOffset, boolean mirror,
                                              boolean takeOpposingPieces, int quadrants) {
        return getPossibleMoves(round, boardPos, player, takeOpposingPieces,
                calculateOffsets(xOffset, yOffset, mirror, quadrants));
    }
    
    public static List<Move> getPossibleMoves(Round round, Square boardPos,
                                              Player player, int xOffset, int yOffset, boolean mirror,
                                              int quadrants) {
        return getPossibleMoves(round, boardPos, player, calculateOffsets(xOffset, yOffset, mirror, quadrants));
    }
    
    public static List<Move> getPossibleMoves(Round round, Square boardPos,
                                              Player player, int xOffset, int yOffset, int quadrants) {
        return getPossibleMoves(round, boardPos, player, xOffset, yOffset, true, quadrants);
    }
    
    public static List<Move> getPossibleMoves(Round round, Square boardPos,
                                              Player player, int xOffset, int yOffset, boolean mirror) {
        return getPossibleMoves(round, boardPos, player, xOffset, yOffset, mirror, QI | QII | QIII | QIV);
    }
    
    public static List<Move> getPossibleMoves(Round round, Square boardPos,
                                              Player player, int xOffset, int yOffset) {
        return getPossibleMoves(round, boardPos, player, xOffset, yOffset, true);
    }
    
}