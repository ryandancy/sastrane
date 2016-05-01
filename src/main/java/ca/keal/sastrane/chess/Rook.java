package ca.keal.sastrane.chess;

import ca.keal.sastrane.Move;
import ca.keal.sastrane.MoveCountingPiece;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.piece.LinePiece;
import ca.keal.sastrane.util.Resource;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
public class Rook extends LinePiece implements MoveCountingPiece {
    
    private int numMoves = 0;
    
    public Rook() {
        super(UP | LEFT | DOWN | RIGHT);
        Chess.getInstance().getBus().register(this);
    }
    
    @Override
    public List<Move> getPossibleMoves(@NonNull Round round, @NonNull Square boardPos, @NonNull Player player) {
        return KingInCheckUtils.getPossibleMoves(super::getPossibleMoves, round, boardPos, player);
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.chess.icon", "rook.png");
    }
    
    @Override
    public void incrementMoveCount() {
        numMoves++;
    }
    
}