package ca.keal.sastrane.api;

import ca.keal.sastrane.api.move.MovingMove;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Wither
public final class Square {
    
    private final int x;
    private final int y;
    
    public MovingMove to(Square to) {
        return new MovingMove(this, to);
    }
    
}