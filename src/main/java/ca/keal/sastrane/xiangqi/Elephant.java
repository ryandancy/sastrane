package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.move.MovingMove;
import ca.keal.sastrane.api.piece.MovingPiece;
import ca.keal.sastrane.util.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Elephant implements MovingPiece {
    
    @Override
    public List<Move> getPossibleMoves(Round round, Square boardPos, Player player) {
        List<MovingMove> moves = new ArrayList<>();
        
        // loop over all possible deltas
        for (int dx = -1; dx <= 1; dx += 2) {
            next: for (int dy = -1; dy <= 1; dy += 2) {
                // skip if either (x+dx, y+dy) or (x+dx*2, y+dy*2) aren't clear
                Square square = null;
                for (int i = 1; i <= 2; i++) {
                    square = new Square(boardPos.getX() + dx * i, boardPos.getY() + dy * i);
                    if (!round.getBoard().isOn(square) || round.getBoard().get(square) != null) {
                        continue next;
                    }
                }
                
                moves.add(boardPos.to(square));
            }
        }
        
        return moves.stream()
                .filter(XiangqiUtils::doesMoveCrossRiver)
                .collect(Collectors.toList());
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.xiangqi.piece", "elephant.png");
    }
    
}