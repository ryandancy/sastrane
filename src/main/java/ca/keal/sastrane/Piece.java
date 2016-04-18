package ca.keal.sastrane;

public interface Piece {
    
    Move[] getPossibleMoves(Board board, Square pos);
    
}