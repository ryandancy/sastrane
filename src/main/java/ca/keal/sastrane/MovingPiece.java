package ca.keal.sastrane;

import lombok.NonNull;

import java.util.List;

public interface MovingPiece extends Piece {
    
    @NonNull
    List<Move> getPossibleMoves(@NonNull Round round, @NonNull Square boardPos, @NonNull Player allegiance);
    
}