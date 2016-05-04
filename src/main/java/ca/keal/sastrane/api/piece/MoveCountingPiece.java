package ca.keal.sastrane.api.piece;

import ca.keal.sastrane.api.event.MoveEvent;
import com.google.common.eventbus.Subscribe;

/**
 * Reduces boilerplate in subscribing to {@link MoveEvent.Post} and incrementing a counter if the move moved this
 * piece.
 */
public interface MoveCountingPiece extends MovingPiece {
    
    void incrementMoveCount();
    
    @Subscribe
    default void afterMove(MoveEvent.Post e) {
        OwnedPiece atEndPos = e.getRound().getBoard().get(e.getMove().getEndPos());
        if (atEndPos != null && this == atEndPos.getPiece()) {
            ((MoveCountingPiece) atEndPos.getPiece()).incrementMoveCount();
        }
    }
    
}