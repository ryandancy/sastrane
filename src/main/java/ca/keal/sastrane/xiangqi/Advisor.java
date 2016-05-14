package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.api.move.Move;
import ca.keal.sastrane.api.piece.JumpingPiece;
import ca.keal.sastrane.api.piece.RecursiveMovingPiece;
import ca.keal.sastrane.util.Resource;

import java.util.List;
import java.util.stream.Collectors;

public class Advisor extends JumpingPiece implements RecursiveMovingPiece {
    
    public Advisor() {
        super(1, 1);
    }
    
    @Override
    public List<Move> getPossibleMoves(Round round, Square boardPos, Player player) {
        return XiangqiUtils.getPossibleMoves(this::getPossibleMovesNonRecursive, round, boardPos, player);
    }
    
    @Override
    public List<Move> getPossibleMovesNonRecursive(Round round, Square boardPos, Player player) {
        assert player instanceof XiangqiPlayer : "Advisor.getPossibleMoves: !(player instanceof XiangqiPlayer)";
        return super.getPossibleMoves(round, boardPos, player).stream()
                .filter(move -> ((XiangqiPlayer) player).getPalace().contains(move.getEndPos()))
                .collect(Collectors.toList());
    }
    
    @Override
    public Resource.Unmangled getImage() {
        return new Resource.Unmangled("ca.keal.sastrane.xiangqi.piece", "advisor.png");
    }
    
}