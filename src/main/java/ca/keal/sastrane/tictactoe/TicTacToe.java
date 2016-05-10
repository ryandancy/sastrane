package ca.keal.sastrane.tictactoe;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Result;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.piece.OwnedPiece;
import ca.keal.sastrane.util.Resource;
import lombok.Getter;

import javax.annotation.Nullable;

public class TicTacToe extends Game {
    
    @Getter private static final TicTacToe instance = new TicTacToe();
    
    public TicTacToe() {
        super("tictactoe", "tictactoe.name",
                new Resource("ca.keal.sastrane.tictactoe", "tictactoe.png"),
                new Resource("ca.keal.sastrane.tictactoe", "tictactoe.css"),
                TicTacToePlayer.values(), TicTacToeAI::new,
                Board.factory()
                        .row("   ")
                        .row("   ")
                        .row("   "),
                new Mark());
    }
    
    @Override
    public Result getResult(Round round) {
        // horizontal + vertical
        for (int maxA = round.getBoard().getMaxX(), maxB = round.getBoard().getMaxY(), i = 0; i < 2; i++) {
            Result result = getStraightLine(maxA, maxB, round);
            if (result != null) {
                return result;
            }
            
            int temp = maxA;
            maxA = maxB;
            maxB = temp;
        }
        
        // diagonal | TODO: reduce repitition here & in getStraightLine
        Player player = null;
        boolean won = true;
        
        for (int x = 0, y = 0; x < round.getBoard().getMaxX() && y < round.getBoard().getMaxY(); x++, y++) {
            Square square = new Square(x, y);
            OwnedPiece atSquare = round.getBoard().get(square);
            if (atSquare == null) {
                won = false;
                break;
            }
    
            if (player == null) {
                player = atSquare.getOwner();
            } else if (player != atSquare.getOwner()) {
                won = false;
                break;
            }
        }
        
        if (won && player != null) {
            return new Result.Win(player);
        }
        
        // board filled = draw
        for (Square square : round.getBoard()) {
            if (round.getBoard().get(square) == null) {
                return Result.NOT_OVER;
            }
        }
        return Result.DRAW;
    }
    
    @Nullable
    private Result getStraightLine(int maxA, int maxB, Round round) {
        for (int a = 0; a < maxA; a++) {
            Player player = null;
            boolean won = true;
            
            for (int b = 0; b < maxB; b++) {
                Square square = new Square(a, b);
                OwnedPiece atSquare = round.getBoard().get(square);
                if (atSquare == null) {
                    won = false;
                    break;
                }
                
                if (player == null) {
                    player = atSquare.getOwner();
                } else if (player != atSquare.getOwner()) {
                    won = false;
                    break;
                }
            }
            
            if (won && player != null) {
                return new Result.Win(player);
            }
        }
        
        return null;
    }
    
}