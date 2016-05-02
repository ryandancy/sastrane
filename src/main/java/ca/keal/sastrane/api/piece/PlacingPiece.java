package ca.keal.sastrane.api.piece;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.move.PlacingMove;

import javax.annotation.Nullable;
import java.util.List;

public interface PlacingPiece extends Piece {
    
    /** player == null => any player */
    List<PlacingMove> getPossiblePlacements(Round round, @Nullable Player player);
    
}
