package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.event.MoveEvent;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.move.MovingMove;
import ca.keal.sastrane.api.piece.JumpingPiece;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.RecursiveMovingPiece;
import ca.keal.sastrane.util.Resource;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;

import java.util.List;

public class Soldier extends JumpingPiece implements RecursiveMovingPiece {
    
    @Getter private boolean acrossRiver = false;
    
    public Soldier() {
        // At start, advance 1
        super(0, 1, QI | QII);
        Xiangqi.getInstance().getBus().register(this);
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
        return new Resource.Unmangled("ca.keal.sastrane.xiangqi.piece", "soldier.png");
    }
    
    @Subscribe
    public void afterMove(MoveEvent.Post e) {
        Square endPos = e.getMove().getEndPos();
        OwnedPiece atEndPos = e.getRound().getBoard().get(endPos);
        if (atEndPos == null || atEndPos.getPiece() != this) return;
        
        if (XiangqiUtils.doesMoveCrossRiver((MovingMove) e.getMove())) {
            // Soldiers get extra moves after they cross the river - can move to sides
            addOffsets(1, 0);
            acrossRiver = true;
        }
    }
    
}