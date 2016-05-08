package ca.keal.sastrane.reversi;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Result;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;
import ca.keal.sastrane.util.Resource;
import com.google.common.collect.Multiset;
import lombok.Getter;

public class Reversi extends Game {
    
    @Getter private static final Reversi instance = new Reversi();
    
    public Reversi() {
        super("reversi", "reversi.name",
                new Resource("ca.keal.sastrane.reversi", "reversi.png"),
                new Resource("ca.keal.sastrane.reversi", "reversi.css"),
                ReversiPlayer.values(), ReversiAI::new,
                Board.factory()
                        .row("        ")
                        .row("        ")
                        .row("        ")
                        .row("   WB   ")
                        .row("   BW   ")
                        .row("        ")
                        .row("        ")
                        .row("        ")
                        .piece('B', new OwnedPieceFactory(Disk::new, ReversiPlayer.BLACK))
                        .piece('W', new OwnedPieceFactory(Disk::new, ReversiPlayer.WHITE)),
                new Disk());
    }
    
    @Override
    public Result getResult(Round round) {
        Player player = ReversiPlayer.BLACK;
        Player opponent = ReversiPlayer.WHITE;
        for (int i = 0; i < 2; i++) {
            if (round.getAllPossibleMoves(player).size() == 0) {
                return count(round.getBoard(), player, opponent);
            }
            
            Player temp = player;
            player = opponent;
            opponent = temp;
        }
        
        return Result.NOT_OVER;
    }
    
    private Result count(Board board, Player player, Player opponent) {
        Multiset<Player> players = ReversiUtils.countPlayers(board);
        switch (Integer.compare(players.count(player), players.count(opponent))) {
            case -1:
                return new Result.Win(opponent);
            case 0:
                return Result.DRAW;
            case 1:
                return new Result.Win(player);
            default:
                // Wait what? This should NEVER happen...
                throw new RuntimeException("Why am I here? It's dark... and I'm scared... help... (Reversi.count)");
        }
    }
    
}