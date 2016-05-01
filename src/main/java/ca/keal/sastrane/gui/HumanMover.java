package ca.keal.sastrane.gui;

import ca.keal.sastrane.api.Decision;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.Mover;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.event.UserMoveEvent;
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