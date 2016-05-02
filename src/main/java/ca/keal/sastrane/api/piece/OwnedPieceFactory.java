package ca.keal.sastrane.api.piece;

import ca.keal.sastrane.api.Player;
import lombok.Data;

import java.util.function.Supplier;

@Data
public class OwnedPieceFactory {
    
    private final Supplier<Piece> pieceFactory;
    private final Player player;
    
}