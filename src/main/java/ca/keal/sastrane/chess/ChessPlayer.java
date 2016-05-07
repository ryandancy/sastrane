package ca.keal.sastrane.chess;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.util.Resource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.BinaryOperator;

@RequiredArgsConstructor
public enum ChessPlayer implements Player {
    
    // y = 0 is at top
    WHITE("white", "chess.player.white", new Resource("ca.keal.sastrane.chess.icon", "white.png"), Player::flipY),
    BLACK("black", "chess.player.black", new Resource("ca.keal.sastrane.chess.icon", "black.png"), Player::flipNone);
    
    @Getter private final String name;
    @Getter private final String i18nName;
    @Getter private final Resource icon;
    private final BinaryOperator<Square> perspectivizer;
    
    @Override
    public Square perspectivize(Square pos, Square original) {
        return perspectivizer.apply(pos, original);
    }
    
}