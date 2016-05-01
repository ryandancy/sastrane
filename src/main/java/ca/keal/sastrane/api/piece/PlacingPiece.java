package ca.keal.sastrane.api.piece;

import ca.keal.sastrane.api.move.PlacingMove;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import lombok.NonNull;

import java.util.List;

public interface PlacingPiece extends Piece {
    
    /** player == null => any player */
    @NonNull
    List<PlacingMove> getPossiblePlacements(@NonNull Round round, @NonNull Player player);
    
}
