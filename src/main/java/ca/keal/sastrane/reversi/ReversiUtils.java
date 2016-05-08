package ca.keal.sastrane.reversi;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.piece.OwnedPiece;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

final class ReversiUtils {
    
    public static Multiset<Player> countPlayers(Board board) {
        Multiset<Player> players = HashMultiset.create();
    
        for (Square square : board) {
            OwnedPiece atSquare = board.get(square);
            if (atSquare != null) {
                players.add(atSquare.getOwner());
            }
        }
        
        return players;
    }
    
}