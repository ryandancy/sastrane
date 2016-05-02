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
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicReference;

// TODO reduce repitition here - there's a *lot* of similarity between getMove() and decide()
@RequiredArgsConstructor
public class HumanMover implements Mover {
    
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
                round.getGame().getBus().register(this);
                controller.setInputting(true);
            }
            
            @Subscribe
            @SuppressFBWarnings("UMAC_UNCALLABLE_METHOD_OF_ANONYMOUS_CLASS")
            public void onUserMove(UserMoveEvent e) {
                if (e.getMover() == HumanMover.this) { // just to be safe
                    controller.setInputting(false);
                    move.set(e.getMove());
                    round.getGame().getBus().unregister(this);
                    
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
                round.getGame().getBus().register(this);
                controller.displayDecision(options);
            }
            
            @Subscribe
            @SuppressFBWarnings("UMAC_UNCALLABLE_METHOD_OF_ANONYMOUS_CLASS")
            public void onUserDecide(UserDecideEvent e) {
                if (e.getMover() == HumanMover.this) { // again, just to be safe
                    decision.set(e.getDecision());
                    round.getGame().getBus().unregister(this);
                    
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