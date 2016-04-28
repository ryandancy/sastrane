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
    
    WHITE("white", new Resource("ca.keal.sastrane.chess.icon", "king_white.png"), Player::flipNone),
    BLACK("black", new Resource("ca.keal.sastrane.chess.icon", "king_black.png"), Player::flipY);
    
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