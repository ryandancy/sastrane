/*
 * Copyright (C) 2016 Ryan Dancy
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 */

package ca.keal.sastrane.api;

import ca.keal.sastrane.api.move.Move;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * The base class of all AIs for all games. Uses the <a href="https://en.wikipedia.org/wiki/Minimax">minimax</a>
 * algorithm combined with <a href="https://en.wikipedia.org/wiki/Alpha-beta_pruning">alpha-beta pruning</a>.
 */
@Getter
@RequiredArgsConstructor
public abstract class AI implements Mover {
    
    protected static final double WIN = Double.MAX_VALUE / 2;
    protected static final double LOSE = -WIN;
    protected static final double DRAW = LOSE / 2;
    
    /** 1 >= difficulty >= 0 */
    private final double difficulty;
    
    private double minimaxAlphaBeta(Round round, int depth, Player player) {
        Set<Player> otherPlayers = Sets.newHashSet(round.getGame().getPlayers());
        otherPlayers.remove(player);
        return minimize(depth, LOSE, WIN, round, player, ImmutableSet.of(player), otherPlayers);
    }
    
    private double minimaxAlphaBeta(Round round, Move move, int depth, Player player) {
        return minimaxAlphaBeta(round.copyWithMove(move), depth, player);
    }
    
    private double maximize(int depth, double a, double b, Round round, Player maximizing,
                            Set<Player> maximizingSet, Set<Player> minimizing) {
        if (depth == 0) {
            return doHeuristic(round, maximizingSet);
        }
        
        List<Move> moves = round.getAllPossibleMoves(maximizing);
        if (moves.size() == 0) {
            return doHeuristic(round, maximizingSet);
        }
        
        double v = LOSE;
        for (Move move : moves) {
            Round roundCopy = round.copyWithMove(move);
            v = Math.max(v, minimize(depth - 1, a, b, roundCopy, maximizing, maximizingSet, minimizing));
            a = Math.max(a, v);
            if (b <= a) break;
        }
        return v;
    }
    
    private double minimize(int depth, double a, double b, Round round, Player maximizing,
                            Set<Player> maximizingSet, Set<Player> minimizing) {
        if (depth == 0) {
            return doHeuristic(round, maximizingSet);
        }
        
        List<Move> moves = new ArrayList<>();
        for (Player player : minimizing) {
            moves.addAll(round.getAllPossibleMoves(player));
        }
        
        if (moves.size() == 0) {
            return doHeuristic(round, maximizingSet);
        }
        
        double v = WIN;
        for (Move move : moves) {
            Round roundCopy = round.copyWithMove(move);
            v = Math.min(v, maximize(depth - 1, a, b, roundCopy, maximizing, maximizingSet, minimizing));
            b = Math.min(b, v);
            if (b <= a) break;
        }
        return v;
    }
    
    @Override
    public Move getMove(Round round, Player player) {
        return round.getAllPossibleMoves(player).stream()
                .max(Comparator.comparing(move -> minimaxAlphaBeta(round, move, getDepth(difficulty), player),
                        Double::compare))
                .get();
    }
    
    @Override
    public Decision decide(Decision[] options, Round round, Player player) {
        return Arrays.stream(options)
                .max(Comparator.comparing(option -> minimaxAlphaBeta(option.whatIf(round), getDepth(difficulty),
                        player), Double::compare))
                .get();
    }
    
    /** depth = 3*difficulty + 1 */
    protected int getDepth(double difficulty) {
        return (int) (3 * difficulty) + 1;
    }
    
    private double doHeuristic(Round round, Set<Player> players) {
        Result result = round.getGame().getArbitrator().arbitrate(round);
        if (result instanceof Result.Win) {
            return players.contains(((Result.Win) result).getPlayer()) ? WIN : LOSE;
        } else if (result == Result.DRAW) {
            return DRAW;
        } else {
            return heuristic(round, players);
        }
    }
    
    protected abstract double heuristic(Round round, Set<Player> players);
    
}