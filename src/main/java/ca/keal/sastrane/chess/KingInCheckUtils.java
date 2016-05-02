package ca.keal.sastrane.chess;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.piece.Piece;
import ca.keal.sastrane.util.Pair;
import ca.keal.sastrane.util.TriFunction;
import ca.keal.sastrane.util.Utils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class KingInCheckUtils {
    
    private KingInCheckUtils() {}
    
    public static Predicate<Move> checkKing(Round round, Player player) {
        return move -> !isKingInCheck(round.copyWithMove(move), player);
    }
    
    public static List<Move> getPossibleMoves(TriFunction<Round, Square, Player, List<Move>> possibleMovesFunc,
                                              Round round, Square boardPos, Player player) {
        return possibleMovesFunc.apply(round, boardPos, player).stream()
                .filter(checkKing(round, player))
                .collect(Collectors.toList());
    }
    
    public static boolean isKingInCheck(Round round, Player player) {
        // Find king, check if in check
        for (Square square : round.getBoard()) {
            Pair<Piece, Player> atSquare = round.getBoard().get(square);
            if (atSquare != null && atSquare.getRight() == player && atSquare.getLeft() instanceof King) {
                return Utils.canBeMovedTo(round, square, player);
            }
        }
        return false;
    }
    
}