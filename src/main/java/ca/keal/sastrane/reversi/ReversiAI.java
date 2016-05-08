package ca.keal.sastrane.reversi;

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Result;
import ca.keal.sastrane.api.Round;
import com.google.common.collect.Multiset;

import java.util.Set;

public class ReversiAI extends AI {
    
    public ReversiAI(double difficulty) {
        super(difficulty);
    }
    
    @Override
    protected double heuristic(Round round, Set<Player> players) {
        // win -> MAX_VALUE, lose -> MIN_VALUE, draw -> MIN_VALUE / 2, else -> # of own disks - # of others' disks
        // Is there a better heuristic possible???
        if (players.size() != 1) throw new IllegalArgumentException("ReversiAI.heuristic: players.size() != 1");
        Player player = players.toArray(new Player[1])[0];
        
        Result result = Reversi.getInstance().getResult(round);
        if (result instanceof Result.Win) {
            return ((Result.Win) result).getPlayer() == player ? Double.MAX_VALUE : Double.MIN_VALUE;
        } else if (result == Result.DRAW) {
            return Double.MIN_VALUE / 2;
        }
        
        Multiset<Player> counts = ReversiUtils.countPlayers(round.getBoard());
        return 2 * counts.count(player) - counts.size(); // equivalent to count(player) - (size() - count(player))
    }
    
}