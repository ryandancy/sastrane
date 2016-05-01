package ca.keal.sastrane.api.piece;

import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.util.Pair;
import lombok.AllArgsConstructor;
import lombok.NonNull;

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
    
    private static final Map<Integer, Pair<Integer, Integer>> directionToDxAndDy = new HashMap<>();
    
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
        directionToDxAndDy.put(UP, Pair.of(0, +1));
        directionToDxAndDy.put(UP_RIGHT, Pair.of(+1, +1));
        directionToDxAndDy.put(RIGHT, Pair.of(+1, 0));
        directionToDxAndDy.put(DOWN_RIGHT, Pair.of(+1, -1));
        directionToDxAndDy.put(DOWN, Pair.of(0, -1));
        directionToDxAndDy.put(DOWN_LEFT, Pair.of(-1, -1));
        directionToDxAndDy.put(LEFT, Pair.of(-1, 0));
        directionToDxAndDy.put(UP_LEFT, Pair.of(-1, +1));
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
    @NonNull
    public List<Move> getPossibleMoves(@NonNull Round round, @NonNull Square boardPos, @NonNull Player allegiance) {
        return getPossibleMoves(round, boardPos, allegiance, stopOnHitPiece, takeOpposingPieces, directions);
    }
    
    @NonNull
    public static List<Move> getPossibleMoves(@NonNull Round round, @NonNull Square boardPos,
                                              @NonNull Player allegiance, boolean stopOnHitPiece,
                                              boolean takeOpposingPieces, int directions) {
        List<Move> res = new ArrayList<>();
        for (int i = 0; i < directionToDxAndDy.size(); i++) {
            if ((directions & (1 << i)) != 0) {
                res.addAll(getMovesInLine(directionToDxAndDy.get(1 << i), round, boardPos, allegiance, stopOnHitPiece,
                        takeOpposingPieces));
            }
        }
        return res;
    }
    
    @NonNull
    private static List<Move> getMovesInLine(Pair<Integer, Integer> dxy, Round round, Square boardPos,
                                             Player allegiance, boolean stopOnHitPiece, boolean takeOpposingPieces) {
        int dx = dxy.getLeft();
        int dy = dxy.getRight();
        List<Move> res = new ArrayList<>();
        
        for (int x = boardPos.getX() + dx, y = boardPos.getY() + dy;
             round.getBoard().isOn(allegiance.perspectivize(new Square(x, y), boardPos));
             x += dx, y += dy) {
            Square square = allegiance.perspectivize(new Square(x, y), boardPos);
            Pair<Piece, Player> atSquare = round.getBoard().get(square);
            if (atSquare == null) {
                res.add(boardPos.to(square));
            } else {
                if (allegiance != atSquare.getRight() && takeOpposingPieces) {
                    res.add(boardPos.to(square));
                }
                if (stopOnHitPiece) break;
            }
        }
        
        return res;
    }
    
    @NonNull
    public static List<Move> getPossibleMoves(@NonNull Round round, @NonNull Square boardPos,
                                              @NonNull Player allegiance, boolean stopOnHitPiece, int directions) {
        return getPossibleMoves(round, boardPos, allegiance, stopOnHitPiece, true, directions);
    }
    
    @NonNull
    public static List<Move> getPossibleMoves(@NonNull Round round, @NonNull Square boardPos,
                                              @NonNull Player allegiance, int directions) {
        return getPossibleMoves(round, boardPos, allegiance, true, directions);
    }
    
}