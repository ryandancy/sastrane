package ca.keal.sastrane.chess;

import ca.keal.sastrane.AI;
import ca.keal.sastrane.Round;
import lombok.NonNull;

public class ChessAI extends AI {
    
    public ChessAI(double difficulty) {
        super(difficulty);
    }
    
    @Override
    public int heuristic(@NonNull Round round) {
        // TODO chess heuristic
        return 0;
    }
    
}