package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.piece.JumpingPiece;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.util.Resource;

import java.util.List;
import java.util.stream.Collectors;

public class General extends JumpingPiece {
    
    public General() {
        super(1, 0, QI | QII | QIII | QIV);
    }
    
    @Override
    public List<Move> getPossibleMoves(Round round, Square boardPos, Player player) {
        assert player instanceof XiangqiPlayer : "General.getPossibleMoves: !(player instanceof XiangqiPlayer)";
        List<Move> moves = super.getPossibleMoves(round, boardPos, player);
        
        // 'Flying General' move: can capture opposing general from across the board if on the same file
        for (int dy = -1; dy <= 1; dy += 2) {
            for (int x = boardPos.getX(), y = boardPos.getY() + dy;
                 round.getBoard().isOn(new Square(x, y));
                 x++, y += dy) {
                Square square = new Square(x, y);
                OwnedPiece atSquare = round.getBoard().get(square);
                if (atSquare != null) {
                    if (atSquare.getPiece() instanceof General) {
                        moves.add(boardPos.to(square));
                    }
                    break;
                }
            }
        }
        
        return moves.stream()
                .filter(move -> ((XiangqiPlayer) player).getPalace().contains(move.getEndPos()))
                .collect(Collectors.toList());
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.xiangqi.piece", "general.png");
    }
    
}