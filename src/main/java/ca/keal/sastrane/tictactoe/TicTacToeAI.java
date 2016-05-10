package ca.keal.sastrane.tictactoe;

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Result;
import ca.keal.sastrane.api.Round;

import java.util.Set;

public class TicTacToeAI extends AI {
    
    public TicTacToeAI(double difficulty) {
        super(difficulty);
    }
    
    @Override
    protected double heuristic(Round round, Set<Player> players) {
        if (players.size() != 1) throw new IllegalArgumentException("TicTacToeAI.heuristic: players.size() != 1");
        Player player = players.toArray(new Player[1])[0];
        
        Result result = TicTacToe.getInstance().getResult(round);
        if (result instanceof Result.Win) {
            return ((Result.Win) result).getPlayer() == player ? WIN : LOSE;
        } else if (result == Result.DRAW) {
            return DRAW;
        } else {
            return 0;
        }
    }
    
}