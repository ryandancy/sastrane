package ca.keal.sastrane.api.move;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Square;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MovingMove implements Move {
    
    @Getter private final Square from;
    @Getter private final Square to;
    
    @Override
    public void move(Board board) {
        if (!board.isOn(from) || !board.isOn(to)) {
            throw new IllegalArgumentException("MovingMove.move: from & to must be on board");
        }
        
        board.set(to, board.get(from));
        board.set(from, null);
    }
    
    @Override
    public Move perspectivize(Player player) {
        return from.to(player.perspectivize(from, to));
    }
    
    @Override
    public Square getEndPos() {
        return to;
    }
    
}