package ca.keal.sastrane;

import lombok.Value;

@Value
public final class Square {
    
    private final int x;
    private final int y;
    
    public Move to(Square to) {
        return new Move(this, to);
    }
    
}