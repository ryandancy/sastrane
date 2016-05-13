package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.util.Resource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.BinaryOperator;

@RequiredArgsConstructor
public enum XiangqiPlayer implements Player {
    
    RED("red", "xiangqi.player.red", new Resource("ca.keal.sastrane.xiangqi.icon", "red.png"), Player::flipY),
    BLACK("black", "xiangqi.player.black", new Resource("ca.keal.sastrane.xiangqi.icon", "black.png"),
            Player::flipNone);
    
    @Getter private final String name;
    @Getter private final String i18nName;
    @Getter private final Resource icon;
    private final BinaryOperator<Square> perspectivizer;
    
    @Override
    public Square perspectivize(Square pos, Square original) {
        return perspectivizer.apply(pos, original);
    }
    
}