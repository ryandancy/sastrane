package ca.keal.sastrane.gui;

import ca.keal.sastrane.Decision;
import ca.keal.sastrane.Move;
import ca.keal.sastrane.Mover;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.event.UserMoveEvent;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class HumanMover implements Mover {
    
    private final GameController controller;
    private AtomicReference<Move> move = new AtomicReference<>(null);
    
    private final Object lock = new Object();
    
    @Override
    @SneakyThrows
    public Move getMove(@NonNull Round round, @NonNull Player player) {
        move.set(null);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                round.getGame().getBus().register(this);
                controller.setInputting(true);
            }
            
            @Subscribe
            public void onUserMove(UserMoveEvent e) {
                if (e.getMover() == HumanMover.this) { // just to be safe
                    controller.setInputting(false);
                    move.set(e.getMove());
                    round.getGame().getBus().unregister(this);
                    
                    // Wake up HumanMover
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                }
            }
        });
        
        synchronized (lock) {
            lock.wait(); // Wait for the user to move
        }
        
        return move.get();
    }
    
    @Override
    public Decision decide(@NonNull Decision[] options, @NonNull Round round, @NonNull Player player) {
        return null;
    }
    
}