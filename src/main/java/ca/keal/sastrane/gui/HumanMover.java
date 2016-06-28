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

package ca.keal.sastrane.gui;

import ca.keal.sastrane.api.Decision;
import ca.keal.sastrane.api.Mover;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.event.UserDecideEvent;
import ca.keal.sastrane.api.event.UserMoveEvent;
import ca.keal.sastrane.api.move.Move;
import com.google.common.eventbus.Subscribe;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.application.Platform;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicReference;

// TODO reduce repitition here - there's a *lot* of similarity between getMove() and decide()
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class HumanMover implements Mover {
    
    private final GameController controller;
    
    private final AtomicReference<Move> move = new AtomicReference<>(null);
    private final AtomicReference<Decision> decision = new AtomicReference<>(null);
    
    private final Object lock = new Object();
    
    @Override
    @SneakyThrows
    public Move getMove(Round round, Player player) {
        move.set(null);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                round.getBus().register(this);
                controller.setInputting(true);
            }
            
            @Subscribe
            @SuppressFBWarnings("UMAC_UNCALLABLE_METHOD_OF_ANONYMOUS_CLASS")
            public void onUserMove(UserMoveEvent e) {
                if (e.getMover() == HumanMover.this) { // just to be safe
                    controller.setInputting(false);
                    move.set(e.getMove());
                    round.getBus().unregister(this);
                    
                    // Wake up HumanMover
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                } // log on else (it's really really bad, there's >1 HumanMover)???
            }
        });
        
        synchronized (lock) {
            while (move.get() == null) {
                lock.wait(); // Wait for the user to decide
            }
        }
        
        return move.get();
    }
    
    @Override
    @SneakyThrows
    public Decision decide(Decision[] options, Round round, Player player) {
        decision.set(null);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                round.getBus().register(this);
                controller.displayDecision(options);
            }
            
            @Subscribe
            @SuppressFBWarnings("UMAC_UNCALLABLE_METHOD_OF_ANONYMOUS_CLASS")
            public void onUserDecide(UserDecideEvent e) {
                if (e.getMover() == HumanMover.this) { // again, just to be safe
                    decision.set(e.getDecision());
                    round.getBus().unregister(this);
                    
                    // Wake up HumanMover
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                } // log on else here too???
            }
        });
        
        synchronized (lock) {
             while (decision.get() == null) {
                lock.wait(); // Wait for the user to decide
            }
        }
        
        return decision.get();
    }
    
}