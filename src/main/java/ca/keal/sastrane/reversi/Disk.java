package ca.keal.sastrane.reversi;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.PlacingMove;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.api.piece.PlacingPiece;
import ca.keal.sastrane.util.Resource;

import java.util.ArrayList;
import java.util.List;

public class Disk implements PlacingPiece {
    
    @Override
    public List<PlacingMove> getPossiblePlacements(Round round, Player player) {
        List<PlacingMove> moves = new ArrayList<>();
        
        for (Square square : round.getBoard()) {
            List<Square> originals = getOriginalsAt(square, round.getBoard(), player);
            if (originals.size() != 0) {
                moves.add(new ReversiMove(player, originals, square));
            }
        }
        
        return moves;
    }
    
    private List<Square> getOriginalsAt(Square square, Board board, Player player) {
        List<Square> originals = new ArrayList<>();
        
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx == 0 && dy == 0) continue;
                
                Square xy = new Square(square.getX() + dx, square.getY() + dy);
                OwnedPiece atXy = board.get(xy);
                while (atXy != null && atXy.getOwner() != player) {
                    xy = new Square(xy.getX() + dx, xy.getY() + dy);
                    atXy = board.get(xy);
                }
                
                if (atXy != null && atXy.getOwner() == player) {
                    originals.add(xy);
                }
            }
        }
        
        return originals;
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.reversi", "disk.png");
    }
    
}