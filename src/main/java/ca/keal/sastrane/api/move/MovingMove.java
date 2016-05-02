package ca.keal.sastrane.api.move;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Square;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
@RequiredArgsConstructor
public class MovingMove implements Move {
    
    private final Square from;
    
    private final Square to;
    
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