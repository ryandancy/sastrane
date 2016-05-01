package ca.keal.sastrane.chess;

import ca.keal.sastrane.Player;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.util.Resource;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.BinaryOperator;

@RequiredArgsConstructor
public enum ChessPlayer implements Player {
    
    // y = 0 is at top
    WHITE("white", new Resource("ca.keal.sastrane.chess.icon", "white.png"), Player::flipY),
    BLACK("black", new Resource("ca.keal.sastrane.chess.icon", "black.png"), Player::flipNone);
    
    @Getter
    private final String name;
    @Getter
    private final Resource icon;
    private final BinaryOperator<Square> perspectivizer;
    
    @Override
    @NonNull
    public Square perspectivize(@NonNull Square pos, @NonNull Square original) {
        return perspectivizer.apply(pos, original);
    }
    
}