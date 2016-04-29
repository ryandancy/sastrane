package ca.keal.sastrane.chess;

import ca.keal.sastrane.Move;
import ca.keal.sastrane.MovingPiece;
import ca.keal.sastrane.Piece;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.util.Pair;
import ca.keal.sastrane.util.Utils;
import lombok.NonNull;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class KingInCheckUtils {
    
    private KingInCheckUtils() {}
    
    @NonNull
    public static Predicate<Move> checkKing(Round round, Player player) {
        return move -> !isKingInCheck(round.copyWithMove(move), player);
    }
    
    @NonNull
    public static List<Move> getPossibleMoves(@NonNull MovingPiece piece, @NonNull Round round,
                                              @NonNull Square boardPos, @NonNull Player player) {
        return piece.getPossibleMoves(round, boardPos, player).stream()
                .filter(checkKing(round, player))
                .collect(Collectors.toList());
    }
    
    public static boolean isKingInCheck(@NonNull Round round, @NonNull Player player) {
        // Find king
        Square kingSquare = null;
        for (Square square : round.getBoard()) {
            Pair<Piece, Player> atSquare = round.getBoard().get(square);
            if (atSquare != null && atSquare.getRight() == player && atSquare.getLeft() instanceof King) {
                kingSquare = square;
                break;
            }
        }
        
        // Check for whether it's in check
        return Utils.canBeMovedTo(round, kingSquare, player);
    }
    
}