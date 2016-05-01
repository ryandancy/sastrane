package ca.keal.sastrane.gui;

import ca.keal.sastrane.Decision;
import ca.keal.sastrane.Move;
import ca.keal.sastrane.Mover;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.Round;
import ca.keal.sastrane.event.UserMoveEvent;
import com.google.common.eventbus.Subscribe;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HumanMover implements Mover {
    
    private final GameController controller;
    private Move move = null;
    
    @Override
    public Move getMove(@NonNull Round round, @NonNull Player player) {
        move = null;
        round.getGame().getBus().register(this);
        
        controller.setInputting(true);
        //noinspection StatementWithEmptyBody
        while (move == null);
        
        round.getGame().getBus().unregister(this);
        return move;
    }
    
    @Override
    public Decision decide(@NonNull Decision[] options, @NonNull Round round, @NonNull Player player) {
        return null;
    }
    
    @Subscribe
    public void onUserMove(UserMoveEvent e) {
        if (e.getMover() == this) { // just to be safe
            move = e.getMove();
        }
    }
    
}