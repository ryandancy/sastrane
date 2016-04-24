package ca.keal.sastrane;

import ca.keal.sastrane.event.MoveEvent;
import ca.keal.sastrane.util.Pair;
import com.google.common.eventbus.Subscribe;

/**
 * Reduces boilerplate in subscribing to {@link MoveEvent.Post} and incrementing a counter if the move moved this
 * piece.
 */
public interface MoveCountingPiece extends MovingPiece {
    
    void incrementMoveCount();
    
    @Subscribe
    default void afterMove(MoveEvent.Post e) {
        Pair<Piece, Player> atEndPos = e.getRound().getBoard().get(e.getMove().getEndPos());
        if (atEndPos != null && this == atEndPos.getLeft()) {
            ((MoveCountingPiece) atEndPos).incrementMoveCount();
        }
    }
    
}