package ca.keal.sastrane;

import lombok.NonNull;
import lombok.Value;

@Value
public final class Move {
    
    @NonNull
    private final Square from;
    
    @NonNull
    private final Square to;
    
}