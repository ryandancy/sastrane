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

package ca.keal.sastrane.reversi;

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.AbstractGameModule;
import ca.keal.sastrane.api.Arbitrator;
import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.GameAttr;
import ca.keal.sastrane.api.Notater;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;
import ca.keal.sastrane.api.piece.PlacingPiece;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString
public class ReversiModule extends AbstractGameModule {
    
    public ReversiModule() {
        super("ca.keal.sastrane.reversi");
    }
    
    @Override
    public void configure() {
        bindToInstance(GameAttr.NAME, String.class, "reversi");
        bindToInstance(GameAttr.PACKAGE, String.class, "ca.keal.sastrane.reversi");
        bindToInstance(GameAttr.PLAYERS, Player[].class, ReversiPlayer.values());
        installFactory(GameAttr.AI, AI.Factory.class, ReversiAI.class);
        bindToInstance(GameAttr.BOARD_FACTORY, Board.Factory.class, Board.factory()
                .row("        ")
                .row("        ")
                .row("        ")
                .row("   WB   ")
                .row("   BW   ")
                .row("        ")
                .row("        ")
                .row("        ")
                .piece('B', new OwnedPieceFactory(Disk::new, ReversiPlayer.BLACK))
                .piece('W', new OwnedPieceFactory(Disk::new, ReversiPlayer.WHITE)));
        bindToInstance(GameAttr.PLACING_PIECES, PlacingPiece[].class, new PlacingPiece[] {new Disk()});
        bindTo(GameAttr.ARBITRATOR, Arbitrator.class, ReversiArbitrator.class);
        bindTo(GameAttr.NOTATER, Notater.class, GridNotater.class);
        bindToInstance(GameAttr.AUTO_PASS, Boolean.class, true);
    
        super.configure();
    }
    
}