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
import ca.keal.sastrane.api.AbstractGameModule;
import ca.keal.sastrane.api.Arbitrator;
import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.GameAttr;
import ca.keal.sastrane.api.Notater;
import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.piece.OwnedPieceFactory;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

public class XiangqiModule extends AbstractGameModule {
    
    public XiangqiModule() {
        super("ca.keal.sastrane.xiangqi");
    }
    
    @Override
    public void configure() {
        bindToInstance(GameAttr.NAME, String.class, "xiangqi");
        bindToInstance(GameAttr.PACKAGE, String.class, "ca.keal.sastrane.xiangqi");
        bindToInstance(GameAttr.PLAYERS, Player[].class, XiangqiPlayer.values());
        installFactory(GameAttr.AI, AI.Factory.class, XiangqiAI.class);
        bindToInstance(GameAttr.BOARD_FACTORY, Board.Factory.class, Board.factory()
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
        bindTo(GameAttr.ARBITRATOR, Arbitrator.class, XiangqiArbitrator.class);
        bindTo(GameAttr.NOTATER, Notater.class, WXFNotater.class);
        bind(PalaceLines.class);
        bindTo(GameAttr.DEFAULTS_REGISTRATOR, new TypeLiteral<Consumer<EventBus>>() {}, DefaultsRegistrator.class);
        
        super.configure();
    }
    
    @RequiredArgsConstructor(onConstructor = @__(@Inject))
    private static class DefaultsRegistrator implements Consumer<EventBus> {
        
        private final PalaceLines lines;
        
        @Override
        public void accept(EventBus bus) {
            bus.register(lines);
        }
        
    }
    
}