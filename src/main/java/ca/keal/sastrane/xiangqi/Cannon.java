package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.piece.LinePiece;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.util.Resource;

import java.util.List;

public class Cannon extends LinePiece {
    
    public Cannon() {
        super(true, false, UP | DOWN | LEFT | RIGHT);
    }
    
    @Override
    public List<Move> getPossibleMoves(Round round, Square boardPos, Player player) {
        List<Move> moves = super.getPossibleMoves(round, boardPos, player);
        
        // Call addCapture in all 4 directions
        for (int i = -1; i <= 1; i += 2) {
            addCapture(moves, round, boardPos, player, i, 0);
            addCapture(moves, round, boardPos, player, 0, i);
        }
        
        return moves;
    }
    
    private void addCapture(List<Move> moves, Round round, Square boardPos, Player player, int dx, int dy) {
        // If possible, add a move to an enemy piece behind any piece (the screen)
        boolean foundScreen = false;
        for (int x = boardPos.getX() + dx, y = boardPos.getY() + dy;
             round.getBoard().isOn(new Square(x, y));
             x += dx, y += dy) {
            Square square = new Square(x, y);
            OwnedPiece atSquare = round.getBoard().get(square);
            if (atSquare == null) continue;
            if (foundScreen) {
                if (atSquare.getOwner() != player) {
                    moves.add(boardPos.to(square));
                }
                return;
            } else {
                foundScreen = true;
            }
        }
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.xiangqi.piece", "cannon.png");
    }
    
}