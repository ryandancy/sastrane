package ca.keal.sastrane.chess;

import ca.keal.sastrane.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChessPlayer implements Player {
    
    WHITE("white"), BLACK("black");
    
    private final String name;
    
}