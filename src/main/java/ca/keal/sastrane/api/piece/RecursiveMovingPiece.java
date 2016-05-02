package ca.keal.sastrane.api.piece;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.util.Utils;

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
    List<Move> getPossibleMovesNonRecursive(Round round, Square boardPos, Player player);
    
}