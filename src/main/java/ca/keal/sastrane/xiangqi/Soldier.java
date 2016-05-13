package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.event.MoveEvent;
import ca.keal.sastrane.api.move.MovingMove;
import ca.keal.sastrane.api.piece.JumpingPiece;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.util.Resource;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;

// TODO register
public class Soldier extends JumpingPiece {
    
    @Getter private boolean acrossRiver = false;
    
    public Soldier() {
        // At start, advance 1
        super(0, 1, QI | QII);
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