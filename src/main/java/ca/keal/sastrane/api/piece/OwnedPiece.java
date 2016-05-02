package ca.keal.sastrane.api.piece;

import ca.keal.sastrane.api.Player;
import lombok.Data;
import lombok.experimental.Wither;

@Data
@Wither
public class OwnedPiece {
    
    private final Piece piece;
    private final Player owner;
    
}