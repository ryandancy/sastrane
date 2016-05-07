package ca.keal.sastrane.chess;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Result;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;
import ca.keal.sastrane.util.Resource;
import lombok.Getter;

public class Chess extends Game {
    
    @Getter
    private static final Chess instance = new Chess();
    
    private Chess() {
        super("ca.keal.sastrane.chess.i18n.chess", "chess.name",
                new Resource("ca.keal.sastrane.chess.icon", "chess.png"),
                new Resource("ca.keal.sastrane.chess", "chess.css"),
                ChessPlayer.values(), ChessAI::new,
                Board.factory()
                        .row("RNBQKBNR")
                        .row("PPPPPPPP")
                        .row("        ")
                        .row("        ")
                        .row("        ")
                        .row("        ")
                        .row("pppppppp")
                        .row("rnbqkbnr")
                        .piece('R', new OwnedPieceFactory(Rook::new, ChessPlayer.BLACK))
                        .piece('N', new OwnedPieceFactory(Knight::new, ChessPlayer.BLACK))
                        .piece('B', new OwnedPieceFactory(Bishop::new, ChessPlayer.BLACK))
                        .piece('Q', new OwnedPieceFactory(Queen::new, ChessPlayer.BLACK))
                        .piece('K', new OwnedPieceFactory(King::new, ChessPlayer.BLACK))
                        .piece('P', new OwnedPieceFactory(Pawn::new, ChessPlayer.BLACK))
                        .piece('n', new OwnedPieceFactory(Knight::new, ChessPlayer.WHITE))
                        .piece('r', new OwnedPieceFactory(Rook::new, ChessPlayer.WHITE))
                        .piece('b', new OwnedPieceFactory(Bishop::new, ChessPlayer.WHITE))
                        .piece('q', new OwnedPieceFactory(Queen::new, ChessPlayer.WHITE))
                        .piece('k', new OwnedPieceFactory(King::new, ChessPlayer.WHITE))
                        .piece('p', new OwnedPieceFactory(Pawn::new, ChessPlayer.WHITE)));
    }
    
    @Override
    public Result getResult(Round round) {
        Player player = ChessPlayer.WHITE;
        Player opponent = ChessPlayer.BLACK;
        for (int i = 0; i < 2; i++) {
            if (round.getAllPossibleMoves(player).size() == 0) {
                if (KingInCheckUtils.isKingInCheck(round, player)) {
                    return new Result.Win(opponent);
                } else {
                    return Result.DRAW;
                }
            }
            
            Player temp = opponent;
            opponent = player;
            player = temp;
        }
        return Result.NOT_OVER;
    }
    
}