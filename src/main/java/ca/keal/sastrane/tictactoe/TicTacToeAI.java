package ca.keal.sastrane.tictactoe;

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;

import java.util.Set;

public class TicTacToeAI extends AI {
    
    public TicTacToeAI(double difficulty) {
        super(difficulty);
    }
    
    @Override
    protected double heuristic(Round round, Set<Player> players) {
        return 0;
    }
    
}