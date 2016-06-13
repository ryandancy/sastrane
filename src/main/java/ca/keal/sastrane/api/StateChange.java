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
import ca.keal.sastrane.api.move.MovingMove;
import ca.keal.sastrane.api.piece.OwnedPiece;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * A descriptor slightly more powerful than {@link Move} -- describes a change of board state due to a {@link Move},
 * containing a {@link Board} for before & after, and the {@link Move} that changed it.
 */
@Data
@RequiredArgsConstructor
public class StateChange {
    
    private final Board before;
    private final Board after;
    private final Move move;
    private final Round afterRound;
    
    public StateChange(Board before, Move move, Round round) {
        this(before, move.whatIf(before), move, round);
    }
    
    /** Use this only if {@code getMove() instanceof MovingMove} */
    public OwnedPiece getMovedPiece() {
        assert move instanceof MovingMove : "getMove() must be a MovingMove in order to use getMovedPiece()";
        OwnedPiece result = before.get(((MovingMove) move).getFrom());
        if (result == null) throw new RuntimeException("lol wut?");
        return result;
    }
    
    /** Convenience method for when you're sure that the move's a {@link MovingMove} */
    public MovingMove getMovingMove() {
        return (MovingMove) move;
    }
    
}