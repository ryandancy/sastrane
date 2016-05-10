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
import java.util.function.BiFunction;

public class TicTacToe extends Game {
    
    @Getter private static final TicTacToe instance = new TicTacToe();
    
    public TicTacToe() {
        super("ca.keal.sastrane.tictactoe.i18n.tictactoe", "tictactoe.name",
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
        BiFunction<Integer, Integer, Square> squareGetter = Square::new;
        for (int maxA = round.getBoard().getMaxX(), maxB = round.getBoard().getMaxY(), i = 0; i < 2; i++) {
            Result result = getStraightLine(maxA, maxB, squareGetter, round);
            if (result != null) return result;
            
            int temp = maxA;
            maxA = maxB;
            maxB = temp;
            squareGetter = (a, b) -> new Square(b, a);
        }
        
        // diagonal
        Result result = getDiagonal(0, round.getBoard().getMaxX(), +1, round);
        if (result != null) return result;
        result = getDiagonal(round.getBoard().getMaxY(), 0, -1, round);
        if (result != null) return result;
        
        // board filled = draw
        for (Square square : round.getBoard()) {
            if (round.getBoard().get(square) == null) {
                return Result.NOT_OVER;
            }
        }
        return Result.DRAW;
    }
    
    @Nullable
    private Result getDiagonal(int startX, int endX, int inc, Round round) {
        // TODO: reduce repitition here & in getStraightLine
        Player player = null;
        boolean won = true;
        
        for (int x = startX, y = 0; x != endX + inc && y <= round.getBoard().getMaxY(); x += inc, y++) {
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
            
        return null;
    }
    
    @Nullable
    private Result getStraightLine(int maxA, int maxB, BiFunction<Integer, Integer, Square> squareGetter, Round round) {
        for (int a = 0; a <= maxA; a++) {
            Player player = null;
            boolean won = true;
            
            for (int b = 0; b <= maxB; b++) {
                Square square = squareGetter.apply(a, b);
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