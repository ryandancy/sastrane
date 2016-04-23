package ca.keal.sastrane.chess;

import ca.keal.sastrane.Move;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.RecursiveMoveResolvingPiece;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.util.Pair;
import ca.keal.sastrane.util.Utils;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class King implements RecursiveMoveResolvingPiece {
    
    @Override
    public List<Move> getPossibleMoves(@NonNull Round round, @NonNull Square boardPos, @NonNull Player allegiance) {
        // All 8 surrounding squares that aren't in the path of any player other than allegiance
        return getPossibleMovesNonRecursive(round, boardPos, allegiance).stream()
                .filter(move -> Utils.canBeMovedTo(round.copyWithMove(move), move.getTo(), allegiance, true))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Move> getPossibleMovesNonRecursive(@NonNull Round round, @NonNull Square boardPos,
                                                   @NonNull Player allegiance) {
        // All 8 surrounding squares
        List<Move> moves = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (!(x == 0 && y == 0) && round.getBoard().isOn(new Square(x, y))) {
                    moves.add(boardPos.to(new Square(x, y)));
                }
            }
        }
        return moves;
    }
    
    @Override
    public Pair<String, String> getPackageAndImageName() {
        // TODO King's icon
        return Pair.of("ca.keal.sastrane.chess.icon", "TODO_king");
    }
    
}