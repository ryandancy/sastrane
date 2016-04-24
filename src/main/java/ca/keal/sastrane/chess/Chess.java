package ca.keal.sastrane.chess;

import ca.keal.sastrane.Board;
import ca.keal.sastrane.Game;
import ca.keal.sastrane.util.Pair;
import lombok.Getter;

@SuppressWarnings("unused")
public class Chess extends Game {
    
    @Getter
    private static final Chess instance = new Chess();
    
    public Chess() {
        super("Chess", Pair.of("ca.keal.sastrane.chess", "chess.png"), ChessPlayer.values(), Board.factory()
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
    
}