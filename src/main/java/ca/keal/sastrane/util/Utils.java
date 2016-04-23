package ca.keal.sastrane.util;

import ca.keal.sastrane.Move;
import ca.keal.sastrane.Piece;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.RecursiveMoveResolvingPiece;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import com.google.common.collect.HashMultiset;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class Utils {
    
    private Utils() {
        throw new RuntimeException("ca.keal.sastrane.util.Utils is a utility class and thus cannot be instantiated.");
    }
    
    /**
     * Returns whether the two collections have equal elements; that is, the same number of elements, and all elements
     * have an equal counterpart on the other set. Disregards order.
     */
    public static <E> boolean areElementsEqual(Collection<E> a, Collection<E> b) {
        return HashMultiset.create(a).equals(HashMultiset.create(b));
    }
    
    public static URL getResource(String filename, String pkg, ClassLoader loader) {
        return loader.getResource(pkg.replace('.', '/') + filename);
    }
    
    public static List<Move> perspectivizeAll(List<Move> moves, Player allegiance) {
        return moves.stream()
                .map(move -> move.getFrom().to(allegiance.perspectivize(move.getFrom(), move.getTo())))
                .collect(Collectors.toList());
    }
    
    /**
     * Returns whether any piece on {@code board} can be moved to {@code square}.
     */
    public static boolean canBeMovedTo(Round round, Square square) {
        for (Square pos : round.getBoard()) {
            Pair<Piece, Player> posData = round.getBoard().get(pos);
            if (posData != null) {
                Piece piece = posData.getLeft();
                Player player = posData.getRight();
                
                List<Move> moves;
                if (piece instanceof RecursiveMoveResolvingPiece) {
                    moves = ((RecursiveMoveResolvingPiece) piece).getPossibleMovesNonRecursive(round, square, player);
                } else {
                    moves = piece.getPossibleMoves(round, square, player);
                }
                
                if (moves.stream().map(Move::getTo).anyMatch(square::equals)) {
                    return true;
                }
            }
        }
        return false;
    }
    
}