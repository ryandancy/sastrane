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

package ca.keal.sastrane.xiangqi;

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;
import ca.keal.sastrane.gui.GuiUtils;
import com.google.inject.Inject;

class Xiangqi extends Game {
    
    private final GuiUtils guiUtils;
    
    @Inject
    Xiangqi(GuiUtils guiUtils) {
        super("xiangqi", "ca.keal.sastrane.xiangqi");
        this.guiUtils = guiUtils;
    }
    
    @Override
    public XiangqiPlayer[] getPlayers() {
        return XiangqiPlayer.values();
    }
    
    @Override
    public AI.Factory getAIFactory() {
        return XiangqiAI::new;
    }
    
    @Override
    public Board.Factory getBoardFactory() {
        return Board.factory()
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
                .piece('s', new OwnedPieceFactory(Soldier::new, XiangqiPlayer.RED));
    }
    
    @Override
    public XiangqiArbitrator getArbitrator() {
        return new XiangqiArbitrator();
    }
    
    @Override
    public WXFNotater getNotater() {
        return new WXFNotater();
    }
    
    @Override
    public PalaceLinesDecor[] getBoardDecor() {
        return new PalaceLinesDecor[] {new PalaceLinesDecor(guiUtils)};
    }
}