package ca.keal.sastrane.api.move;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Square;
import lombok.NonNull;

public interface Move {
    
    void move(@NonNull Board board);
    
    @NonNull
    Move perspectivize(@NonNull Player player);
    
    /**
     * Returns the ending position of the move; that is, the position this move's affected piece will have. For example,
     * in chess, the {@code getEndPos} of the {@link Move} that describes the pawn's single space move forwards will
     * return the {@link Square} that represents the space in front of the pawn.
     */
    @NonNull
    Square getEndPos();
    
}