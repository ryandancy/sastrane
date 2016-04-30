package ca.keal.sastrane.chess;

import ca.keal.sastrane.Board;
import ca.keal.sastrane.Game;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.Result;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.util.Pair;
import ca.keal.sastrane.util.Resource;
import lombok.Getter;
import lombok.NonNull;

public class Chess extends Game {
    
    @Getter
    private static final Chess instance = new Chess();
    
    public Chess() {
        super("Chess", new Resource("ca.keal.sastrane.chess", "chess.png"),
                new Resource("ca.keal.sastrane.chess", "chess.css"), ChessPlayer.values(), ChessAI::new, Board.factory()
                .row("RNBQKBNR")
                .row("PPPPPPPP")
                .row("        ")
                .row("        ")
                .row("        ")
                .row("        ")
                .row("pppppppp")
                .row("rnbqkbnr")
                .piece('R', Pair.of(Rook::new, ChessPlayer.BLACK))
                .piece('N', Pair.of(Knight::new, ChessPlayer.BLACK))
                .piece('B', Pair.of(Bishop::new, ChessPlayer.BLACK))
                .piece('Q', Pair.of(Queen::new, ChessPlayer.BLACK))
                .piece('K', Pair.of(King::new, ChessPlayer.BLACK))
                .piece('P', Pair.of(Pawn::new, ChessPlayer.BLACK))
                .piece('n', Pair.of(Knight::new, ChessPlayer.WHITE))
                .piece('r', Pair.of(Rook::new, ChessPlayer.WHITE))
                .piece('b', Pair.of(Bishop::new, ChessPlayer.WHITE))
                .piece('q', Pair.of(Queen::new, ChessPlayer.WHITE))
                .piece('k', Pair.of(King::new, ChessPlayer.WHITE))
                .piece('p', Pair.of(Pawn::new, ChessPlayer.WHITE)));
    }
    
    @Override
    @NonNull
    public Result getResult(@NonNull Round round) {
        for (Player player : getPlayers()) {
            if (round.getAllPossibleMoves(player).size() == 0) {
                return KingInCheckUtils.isKingInCheck(round, player) ? new Result.Win(player) : Result.DRAW;
            }
        }
        return Result.NOT_OVER;
    }
    
}