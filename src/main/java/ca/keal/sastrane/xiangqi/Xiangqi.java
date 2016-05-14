package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.Result;
import ca.keal.sastrane.api.Round;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;
import ca.keal.sastrane.util.Resource;
import lombok.Getter;

public class Xiangqi extends Game {
    
    @Getter private static final Xiangqi instance = new Xiangqi();
    
    public Xiangqi() {
        super("ca.keal.sastrane.xiangqi.i18n.xiangqi", "xiangqi.name",
                new Resource("ca.keal.sastrane.xiangqi.icon", "xiangqi.png"),
                new Resource("ca.keal.sastrane.xiangqi", "xiangqi.css"),
                XiangqiPlayer.values(), XiangqiAI::new,
                Board.factory()
                        .row("RHEAGAEHR")
                        .row("         ")
                        .row(" C     C ")
                        .row("S S S S S")
                        .row("         ")
                        .row("         ")
                        .row("s s s s s")
                        .row(" c     c ")
                        .row("         ")
                        .row("rheagaehr")
                        .piece('R', new OwnedPieceFactory(Chariot::new, XiangqiPlayer.BLACK))
                        .piece('H', new OwnedPieceFactory(Horse::new, XiangqiPlayer.BLACK))
                        .piece('E', new OwnedPieceFactory(Elephant::new, XiangqiPlayer.BLACK))
                        .piece('A', new OwnedPieceFactory(Advisor::new, XiangqiPlayer.BLACK))
                        .piece('G', new OwnedPieceFactory(General::new, XiangqiPlayer.BLACK))
                        .piece('C', new OwnedPieceFactory(Cannon::new, XiangqiPlayer.BLACK))
                        .piece('S', new OwnedPieceFactory(Soldier::new, XiangqiPlayer.BLACK))
                        .piece('r', new OwnedPieceFactory(Chariot::new, XiangqiPlayer.RED))
                        .piece('h', new OwnedPieceFactory(Horse::new, XiangqiPlayer.RED))
                        .piece('e', new OwnedPieceFactory(Elephant::new, XiangqiPlayer.RED))
                        .piece('a', new OwnedPieceFactory(Advisor::new, XiangqiPlayer.RED))
                        .piece('g', new OwnedPieceFactory(General::new, XiangqiPlayer.RED))
                        .piece('c', new OwnedPieceFactory(Cannon::new, XiangqiPlayer.RED))
                        .piece('s', new OwnedPieceFactory(Soldier::new, XiangqiPlayer.RED)));
    }
    
    @Override
    public Result getResult(Round round) {
        // TODO
        return null;
    }
}