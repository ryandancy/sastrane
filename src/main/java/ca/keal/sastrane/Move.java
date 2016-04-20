package ca.keal.sastrane;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@RequiredArgsConstructor
public final class Move {
    
    @NonNull
    private final Square from;
    
    @NonNull
    private final Square to;
    
    @NonFinal
    private boolean moved = false;
    
    public void move(@NonNull Board board) {
        if (moved) {
            throw new IllegalStateException("Move is already moved.");
        } else {
            moved = true;
        }
        
        if (!board.isOn(from) || !board.isOn(to)) {
            throw new IllegalArgumentException("Move.move: from & to must be on board");
        }
        
        board.set(to, board.get(from));
        board.set(from, null);
    }
    
}