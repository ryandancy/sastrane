package ca.keal.sastrane;

import lombok.NonNull;

public interface Piece {
    
    @NonNull
    Move[] getPossibleMoves(@NonNull Game game, @NonNull Square boardPos);
    
}