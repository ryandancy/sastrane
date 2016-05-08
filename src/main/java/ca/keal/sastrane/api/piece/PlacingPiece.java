package ca.keal.sastrane.api.piece;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.move.PlacingMove;

import java.util.List;

public interface PlacingPiece extends Piece {
    
    List<PlacingMove> getPossiblePlacements(Round round, Player player);
    
}
