package ca.keal.sastrane.chess;

import ca.keal.sastrane.Move;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.piece.LinePiece;
import ca.keal.sastrane.util.Resource;
import lombok.NonNull;

import java.util.List;

public class Queen extends LinePiece {
    
    public Queen() {
        super(UP | UP_RIGHT | RIGHT | DOWN_RIGHT | DOWN | DOWN_LEFT | LEFT | UP_LEFT);
    }
    
    @Override
    public List<Move> getPossibleMoves(@NonNull Round round, @NonNull Square boardPos, @NonNull Player player) {
        return KingInCheckUtils.getPossibleMoves(this, round, boardPos, player);
    }
    
    @Override
    public Resource getImage() {
        return new Resource("ca.keal.sastrane.chess.icon", "queen");
    }
    
}