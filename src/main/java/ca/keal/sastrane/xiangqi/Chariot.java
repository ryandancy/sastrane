package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.piece.LinePiece;
import ca.keal.sastrane.util.Resource;

public class Chariot extends LinePiece {
    
    public Chariot() {
        super(UP | LEFT | DOWN | RIGHT);
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.xiangqi.piece", "chariot.png");
    }
    
}