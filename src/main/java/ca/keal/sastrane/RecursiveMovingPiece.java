package ca.keal.sastrane;

import ca.keal.sastrane.util.Utils;
import lombok.NonNull;

import java.util.List;

/**
 * A subinterface of Piece that represents a piece that resolves its possible moves recursively, generally using {@link
 * Utils#canBeMovedTo(Round, Square)}.
 */
public interface RecursiveMovingPiece extends MovingPiece {
    
    /**
     * Returns all possible moves this piece could make, without using recursion/{@link Utils#canBeMovedTo(Round,
     * Square)}.
     */
    @NonNull
    List<Move> getPossibleMovesNonRecursive(@NonNull Round round, @NonNull Square boardPos, @NonNull Player player);
    
}