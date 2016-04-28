package ca.keal.sastrane;

import lombok.NonNull;

import java.util.List;

public interface PlacingPiece {
    
    @NonNull
    List<PlacingMove> getPossiblePlacements(@NonNull Round round);
    
}
