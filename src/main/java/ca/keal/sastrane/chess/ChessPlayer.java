package ca.keal.sastrane.chess;

import ca.keal.sastrane.Player;
import ca.keal.sastrane.Square;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.BinaryOperator;

@RequiredArgsConstructor
public enum ChessPlayer implements Player {
    
    WHITE("white", Player::flipNone), BLACK("black", Player::flipY);
    
    @Getter
    private final String name;
    private final BinaryOperator<Square> perspectivizer;
    
    @Override
    @NonNull
    public Square perspectivize(@NonNull Square pos, @NonNull Square original) {
        return perspectivizer.apply(pos, original);
    }
    
}