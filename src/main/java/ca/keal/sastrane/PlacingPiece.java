package ca.keal.sastrane;

import lombok.NonNull;

import java.util.List;

public interface PlacingPiece extends Piece {
    
    /** player == null => any player */
    @NonNull
    List<PlacingMove> getPossiblePlacements(@NonNull Round round, @NonNull Player player);
    
}
