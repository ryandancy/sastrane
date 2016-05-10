package ca.keal.sastrane.tictactoe;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.PlacingMove;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.PlacingPiece;
import ca.keal.sastrane.util.Resource;

import java.util.ArrayList;
import java.util.List;

public class Mark implements PlacingPiece {
    
    @Override
    public List<PlacingMove> getPossiblePlacements(Round round, Player player) {
        List<PlacingMove> moves = new ArrayList<>();
        for (Square square : round.getBoard()) {
            if (round.getBoard().get(square) == null) {
                moves.add(new PlacingMove(new OwnedPiece(new Mark(), player), square));
            }
        }
        return moves;
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.tictactoe", "mark.png");
    }
    
}