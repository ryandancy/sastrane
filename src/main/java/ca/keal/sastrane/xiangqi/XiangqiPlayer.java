package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.util.Resource;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.BinaryOperator;

@RequiredArgsConstructor
public enum XiangqiPlayer implements Player {
    
    RED("red", "xiangqi.player.red", new Resource("ca.keal.sastrane.xiangqi.piece", "general_red.png"), Player::flipY,
            ImmutableList.of(
                new Square(3, 9), new Square(4, 9), new Square(5, 9),
                new Square(3, 8), new Square(4, 8), new Square(5, 8),
                new Square(3, 7), new Square(4, 7), new Square(5, 7))),
    BLACK("black", "xiangqi.player.black", new Resource("ca.keal.sastrane.xiangqi.piece", "general_black.png"),
            Player::flipNone, ImmutableList.of(
                new Square(3, 2), new Square(4, 2), new Square(5, 2),
                new Square(3, 1), new Square(4, 1), new Square(5, 1),
                new Square(3, 0), new Square(4, 0), new Square(5, 0)));
    
    @Getter private final String name;
    @Getter private final String i18nName;
    @Getter private final Resource icon;
    private final BinaryOperator<Square> perspectivizer;
    @Getter private final List<Square> palace;
    
    @Override
    public Square perspectivize(Square pos, Square original) {
        return perspectivizer.apply(pos, original);
    }
    
}