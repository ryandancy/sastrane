package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.piece.LinePiece;
import ca.keal.sastrane.api.piece.RecursiveMovingPiece;
import ca.keal.sastrane.util.Resource;

import java.util.List;

public class Chariot extends LinePiece implements RecursiveMovingPiece {
    
    public Chariot() {
        super(UP | LEFT | DOWN | RIGHT);
    }
    
    @Override
    public List<Move> getPossibleMoves(Round round, Square boardPos, Player player) {
        return XiangqiUtils.getPossibleMoves(this::getPossibleMovesNonRecursive, round, boardPos, player);
    }
    
    @Override
    public List<Move> getPossibleMovesNonRecursive(Round round, Square boardPos, Player player) {
        return super.getPossibleMoves(round, boardPos, player);
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.xiangqi.piece", "chariot.png");
    }
    
}