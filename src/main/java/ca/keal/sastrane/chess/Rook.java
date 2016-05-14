package ca.keal.sastrane.chess;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.piece.LinePiece;
import ca.keal.sastrane.api.piece.MoveCountingPiece;
import ca.keal.sastrane.api.piece.RecursiveMovingPiece;
import ca.keal.sastrane.util.Resource;
import lombok.Getter;

import java.util.List;

@Getter
public class Rook extends LinePiece implements MoveCountingPiece, RecursiveMovingPiece {
    
    private int numMoves = 0;
    
    public Rook() {
        super(UP | LEFT | DOWN | RIGHT);
        Chess.getInstance().getBus().register(this);
    }
    
    @Override
    public List<Move> getPossibleMoves(Round round, Square boardPos, Player player) {
        return KingInCheckUtils.getPossibleMoves(super::getPossibleMoves, round, boardPos, player);
    }
    
    @Override
    public List<Move> getPossibleMovesNonRecursive(Round round, Square boardPos, Player player) {
        return super.getPossibleMoves(round, boardPos, player);
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.chess.piece", "rook.png");
    }
    
    @Override
    public void incrementMoveCount() {
        numMoves++;
    }
    
}