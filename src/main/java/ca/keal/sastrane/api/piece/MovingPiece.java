package ca.keal.sastrane.api.piece;

import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import lombok.NonNull;

import java.util.List;

public interface MovingPiece extends Piece {
    
    @NonNull
    List<Move> getPossibleMoves(@NonNull Round round, @NonNull Square boardPos, @NonNull Player player);
    
}