package ca.keal.sastrane.chess;

import ca.keal.sastrane.Move;
import ca.keal.sastrane.MovingPiece;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.util.Utils;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@UtilityClass
public class KingInCheckUtils {
    
    private KingInCheckUtils() {}
    
    public static Predicate<Move> checkKing(Round round, Player player) {
        return move -> !Utils.canBeMovedTo(round.copyWithMove(move), move.getEndPos(), player);
    }
    
    public static List<Move> getPossibleMoves(@NonNull MovingPiece piece, @NonNull Round round,
                                              @NonNull Square boardPos, @NonNull Player player) {
        return piece.getPossibleMoves(round, boardPos, player).stream()
                .filter(checkKing(round, player))
                .collect(Collectors.toList());
    }
    
}