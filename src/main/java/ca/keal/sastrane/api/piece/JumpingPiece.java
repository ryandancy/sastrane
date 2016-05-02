package ca.keal.sastrane.api.piece;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JumpingPiece implements MovingPiece {
    
    private static final Map<Integer, Pair<Integer, Integer>> quadrantsToSigns = new HashMap<>();
    
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
        quadrantsToSigns.put(QI, Pair.of(+1, +1));
        quadrantsToSigns.put(QII, Pair.of(-1, +1));
        quadrantsToSigns.put(QIII, Pair.of(-1, -1));
        quadrantsToSigns.put(QIV, Pair.of(+1, -1));
    }
    
    private final List<Pair<Integer, Integer>> offsets;
    private final boolean takeOpposingPieces;
    
    public JumpingPiece(int xOffset, int yOffset, boolean mirror, boolean takeOpposingPieces, int quadrants) {
        this.takeOpposingPieces = takeOpposingPieces;
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
    
    @Override
    public List<Move> getPossibleMoves(Round round, Square boardPos, Player player) {
        return getPossibleMoves(round, boardPos, player, takeOpposingPieces, offsets);
    }
    
    @SuppressWarnings("SuspiciousNameCombination")
    public static List<Pair<Integer, Integer>> calculateOffsets(int xOffset, int yOffset, boolean mirror,
                                                                int quadrants) {
        List<Pair<Integer, Integer>> offsets = new ArrayList<>();
        
        // The ^= stuff swaps x and y
        for (int x = xOffset, y = yOffset, i = 0; i < (mirror ? 2 : 1); x ^= y, y ^= x, x ^= y, i++) {
            for (int j = 0; j < quadrantsToSigns.size(); j++) {
                if ((quadrants & (1 << j)) != 0) {
                    Pair<Integer, Integer> signs = quadrantsToSigns.get(1 << j);
                    offsets.add(Pair.of(signs.getLeft() * x, signs.getRight() * y));
                }
            }
        }
        
        return offsets;
    }
    
    public static List<Move> getPossibleMoves(Round round, Square boardPos,
                                              Player player, boolean takeOpposingPieces,
                                              List<Pair<Integer, Integer>> offsets) {
        List<Move> res = new ArrayList<>();
        for (Pair<Integer, Integer> offset : offsets) {
            Square deltaPos = player.perspectivize(new Square(boardPos.getX() + offset.getLeft(),
                    boardPos.getY() + offset.getRight()), boardPos);
            if (!round.getBoard().isOn(deltaPos)) continue;
            
            Pair<Piece, Player> atDelta = round.getBoard().get(deltaPos);
            if (atDelta == null || (player != atDelta.getRight() && takeOpposingPieces)) {
                res.add(boardPos.to(deltaPos));
            }
        }
        return res;
    }
    
    public static List<Move> getPossibleMoves(Round round, Square boardPos,
                                              Player player,
                                              List<Pair<Integer, Integer>> offsets) {
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