package ca.keal.sastrane;

import lombok.NonNull;
import lombok.Value;

@Value
public final class Square {
    
    private final int x;
    private final int y;
    
    @NonNull
    public Move to(@NonNull Square to) {
        return new Move(this, to);
    }
    
}