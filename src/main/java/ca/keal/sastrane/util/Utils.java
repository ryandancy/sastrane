package ca.keal.sastrane.util;

import ca.keal.sastrane.Move;
import ca.keal.sastrane.MovingPiece;
import ca.keal.sastrane.Piece;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.RecursiveMovingPiece;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import com.google.common.collect.HashMultiset;
import lombok.experimental.UtilityClass;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class Utils {
    
    private Utils() {}
    
    /**
     * Returns whether the two collections have equal elements; that is, the same number of elements, and all elements
     * have an equal counterpart on the other set. Disregards order.
     */
    public static <E> boolean areElementsEqual(Collection<E> a, Collection<E> b) {
        return HashMultiset.create(a).equals(HashMultiset.create(b));
    }
    
    public static URL getResource(String filename, String pkg, ClassLoader loader) {
        return loader.getResource(pkg.replace('.', '/') + "/" + filename);
    }
    
    public static List<Move> perspectivizeAll(List<? extends Move> moves, Player allegiance) {
        return moves.stream()
                .map(move -> move.perspectivize(allegiance))
                .collect(Collectors.toList());
    }
    
    /**
     * Returns whether any piece on {@code board} belonging to {@code player} (or not) can be moved to {@code square}.
     * {@code player == null} means any player is acceptable. If invert, then any player <i>not</i> the specified
     * player's pieces will be matched; else, <i>only</i> the specified player's pieces will be matched.
     */
    public static boolean canBeMovedTo(Round round, Square square, Player player, boolean invert) {
        for (Square pos : round.getBoard()) {
            Pair<Piece, Player> posData = round.getBoard().get(pos);
            if (posData != null) {
                Piece piece = posData.getLeft();
                Player piecePlayer = posData.getRight();
                
                if (piece instanceof MovingPiece && (player == null || (invert ? player != piecePlayer
                        : player == piecePlayer))) {
                    List<Move> moves;
                    if (piece instanceof RecursiveMovingPiece) {
                        moves = ((RecursiveMovingPiece) piece)
                                .getPossibleMovesNonRecursive(round, square, piecePlayer);
                    } else {
                        moves = ((MovingPiece) piece).getPossibleMoves(round, square, piecePlayer);
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