package ca.keal.sastrane.chess;

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Result;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.piece.Piece;
import ca.keal.sastrane.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class ChessAI extends AI {
    
    private static final Map<Class<? extends Piece>, Double> pieceToNaiveValue = new HashMap<>();
    
    static {
        pieceToNaiveValue.put(Pawn.class, 1.0);
        pieceToNaiveValue.put(Knight.class, 3.0);
        pieceToNaiveValue.put(Bishop.class, 3.0);
        pieceToNaiveValue.put(Rook.class, 5.0);
        pieceToNaiveValue.put(Queen.class, 9.0);
        pieceToNaiveValue.put(King.class, 1e9); // not Double.MAX_VALUE to avoid overflow
    }
    
    public ChessAI(double difficulty) {
        super(difficulty);
    }
    
    @Override
    public double heuristic(Round round, Player player) {
        // draw => MIN_VALUE/2, win => MAX_VALUE, lose => MIN_VALUE, else: naive piece value method
        // TODO improve piece valuation over naive method
        Result result = Chess.getInstance().getResult(round);
        if (result instanceof Result.Win) {
            return ((Result.Win) result).getPlayer() == player ? Double.MAX_VALUE : Double.MIN_VALUE;
        } else if (result == Result.DRAW) {
            return Double.MIN_VALUE / 2;
        }
        
        // value = sum of own pieces - sum of everyone else's pieces
        double sumOwnPieces = 0;
        double sumOpponentPieces = 0;
        for (Square square : round.getBoard()) {
            Pair<Piece, Player> atSquare = round.getBoard().get(square);
            if (atSquare != null) {
                double naiveValue = pieceToNaiveValue.get(atSquare.getLeft().getClass());
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