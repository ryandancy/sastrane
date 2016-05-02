package ca.keal.sastrane.api.piece;

import lombok.Data;
import lombok.experimental.Wither;

@Data
@Wither
class Offset {
    
    private final int dx;
    private final int dy;
    
}