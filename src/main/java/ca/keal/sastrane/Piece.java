package ca.keal.sastrane;

import lombok.NonNull;

public interface Piece {
    
    @NonNull
    Move[] getPossibleMoves(@NonNull Board board, @NonNull Square pos);
    
}