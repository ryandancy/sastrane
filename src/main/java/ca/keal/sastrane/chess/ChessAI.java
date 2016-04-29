package ca.keal.sastrane.chess;

import ca.keal.sastrane.AI;
import ca.keal.sastrane.Piece;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.Result;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.Square;
import ca.keal.sastrane.util.Pair;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ChessAI extends AI {
    
    // TODO migrate AI to double instead of int
    private static final Map<Class<? extends Piece>, Integer> pieceToNaiveValue = new HashMap<>();
    
    static {
        pieceToNaiveValue.put(Pawn.class, 1);
        pieceToNaiveValue.put(Knight.class, 3);
        pieceToNaiveValue.put(Bishop.class, 3);
        pieceToNaiveValue.put(Rook.class, 5);
        pieceToNaiveValue.put(Queen.class, 9);
        pieceToNaiveValue.put(King.class, (int) 1e9); // not Integer.MAX_VALUE to avoid overflow
    }
    
    public ChessAI(double difficulty) {
        super(difficulty);
    }
    
    @Override
    public int heuristic(@NonNull Round round, @NonNull Player player) {
        // draw => MIN_VALUE/2, win => MAX_VALUE, lose => MIN_VALUE, else: naive piece value method
        // TODO improve piece valuation over naive method
        Result result = Chess.getInstance().getResult(round);
        if (result instanceof Result.Win) {
            return ((Result.Win) result).getPlayer() == player ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        } else if (result == Result.DRAW) {
            return Integer.MIN_VALUE / 2;
        }
        
        // value = sum of own pieces - sum of everyone else's pieces
        int sumOwnPieces = 0;
        int sumOpponentPieces = 0;
        for (Square square : round.getBoard()) {
            Pair<Piece, Player> atSquare = round.getBoard().get(square);
            if (atSquare != null) {
                int naiveValue = pieceToNaiveValue.get(atSquare.getLeft().getClass());
                if (atSquare.getRight() == player) {
                    sumOwnPieces += naiveValue;
                } else {
                    sumOpponentPieces += naiveValue;
                }
            }
        }
        
        return sumOwnPieces - sumOpponentPieces;
    }
    
}