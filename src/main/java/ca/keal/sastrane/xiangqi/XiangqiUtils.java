package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.move.MovingMove;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.util.TriFunction;
import ca.keal.sastrane.util.Utils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

final class XiangqiUtils {
    
    /** river is between ranks 4 and 5 */
    private static final int RIVER_Y = 4;
    
    private XiangqiUtils() {
        throw new IllegalStateException("XiangqiUtils is a utility class and therefore cannot be constructed.");
    }
    
    public static boolean doesMoveCrossRiver(MovingMove move) {
        return (move.getFrom().getY() > RIVER_Y) != (move.getTo().getY() > RIVER_Y);
    }
    
    // The below is copy-pasted from KingInCheckUtils: merge into common class somehow???
    
    public static Predicate<Move> checkGeneral(Round round, Player player) {
        return move -> !isGeneralInCheck(round.copyWithMove(move), player);
    }
    
    public static List<Move> getPossibleMoves(TriFunction<Round, Square, Player, List<Move>> possibleMovesFunc,
                                              Round round, Square boardPos, Player player) {
        return possibleMovesFunc.apply(round, boardPos, player).stream()
                .filter(checkGeneral(round, player))
                .collect(Collectors.toList());
    }
    
    public static boolean isGeneralInCheck(Round round, Player player) {
        // Find general, check if in check
        for (Square square : round.getBoard()) {
            OwnedPiece atSquare = round.getBoard().get(square);
            if (atSquare != null && atSquare.getOwner() == player && atSquare.getPiece() instanceof General) {
                return Utils.canBeMovedTo(round, square, player);
            }
        }
        return false;
    }
    
}