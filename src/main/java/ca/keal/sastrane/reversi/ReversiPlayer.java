package ca.keal.sastrane.reversi;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.util.Resource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReversiPlayer implements Player {
    
    BLACK("black", "reversi.player.black", new Resource("ca.keal.sastrane.reversi", "disk_black.png")),
    WHITE("white", "reversi.player.white", new Resource("ca.keal.sastrane.reversi", "disk_white.png"));
    
    private final String name;
    private final String i18nName;
    private final Resource icon;
    
    @Override
    public Square perspectivize(Square pos, Square original) {
        return pos;
    }
    
}
