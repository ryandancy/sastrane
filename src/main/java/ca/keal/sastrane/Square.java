package ca.keal.sastrane;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Wither
public final class Square {
    
    private final int x;
    private final int y;
    
    @NonNull
    public MovingMove to(@NonNull Square to) {
        return new MovingMove(this, to);
    }
    
}