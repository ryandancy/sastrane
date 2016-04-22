package ca.keal.sastrane;

import lombok.NonNull;

public interface Piece {
    
    @NonNull
    Move[] getPossibleMoves(@NonNull Round round, @NonNull Square boardPos);
    
}