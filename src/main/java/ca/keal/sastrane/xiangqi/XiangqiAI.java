package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;

import java.util.Set;

public class XiangqiAI extends AI {
    
    public XiangqiAI(double difficulty) {
        super(difficulty);
    }
    
    @Override
    protected double heuristic(Round round, Set<Player> players) {
        // TODO
        return 0;
    }
    
}