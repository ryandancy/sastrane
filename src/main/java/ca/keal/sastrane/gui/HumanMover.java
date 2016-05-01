package ca.keal.sastrane.gui;

import ca.keal.sastrane.Decision;
import ca.keal.sastrane.Move;
import ca.keal.sastrane.Mover;
import ca.keal.sastrane.Player;
import ca.keal.sastrane.Round;
import lombok.NonNull;

public class HumanMover implements Mover {
    
    @Override
    public Move getMove(@NonNull Round round, @NonNull Player player) {
        return null;
    }
    
    @Override
    public Decision decide(@NonNull Decision[] options, @NonNull Round round, @NonNull Player player) {
        return null;
    }
    
}