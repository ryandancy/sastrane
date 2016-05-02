package ca.keal.sastrane.api.piece;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.Move;

import java.util.List;

public interface MovingPiece extends Piece {
    
    List<Move> getPossibleMoves(Round round, Square boardPos, Player player);
    
}