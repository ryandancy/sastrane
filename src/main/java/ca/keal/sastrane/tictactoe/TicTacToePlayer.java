package ca.keal.sastrane.tictactoe;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.util.Resource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TicTacToePlayer implements Player {
    
    X("x", "tictactoe.player.x", new Resource("ca.keal.sastrane.tictactoe", "mark_x.png")),
    O("o", "tictactoe.player.o", new Resource("ca.keal.sastrane.tictactoe", "mark_o.png"));
    
    private final String name;
    private final String i18nName;
    private final Resource icon;
    
    @Override
    public Square perspectivize(Square pos, Square original) {
        return pos;
    }
    
}