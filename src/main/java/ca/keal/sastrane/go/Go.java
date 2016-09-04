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

package ca.keal.sastrane.go;

import ca.keal.sastrane.api.AI;
import ca.keal.sastrane.api.Board;
import ca.keal.sastrane.api.Game;
import ca.keal.sastrane.api.event.ToGameEvent;
import ca.keal.sastrane.gui.GuiUtils;
import ca.keal.sastrane.util.Utils;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.scene.layout.Pane;

import java.util.function.Consumer;

class Go extends Game {
    
    private final double komi;
    private final GuiUtils guiUtils;
    
    @Inject
    Go(@Named("komi") double komi, GuiUtils guiUtils) {
        super("go", "ca.keal.sastrane.go");
        this.komi = komi;
        this.guiUtils = guiUtils;
    }
    
    @Override
    public GoPlayer[] getPlayers() {
        return GoPlayer.values();
    }
    
    @Override
    public AI.Factory getAIFactory() {
        return Utils.instantiateFactory(AI.Factory.class, GoAI.class);
    }
    
    @Override
    public Board.Factory getBoardFactory() {
        return Board.factory()
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ") // A 19x19 empty square
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ")
                .row("                   ");
    }
    
    @Override
    public GoArbitrator getArbitrator() {
        return new GoArbitrator(komi);
    }
    
    @Override
    public Stone[] getPlacingPieces() {
        return new Stone[] {new Stone()};
    }
    
    @Override
    public boolean allowPassing() {
        return true;
    }
    
    @Override
    public StarPointsDecor[] getBoardDecor() {
        return new StarPointsDecor[] {new StarPointsDecor(guiUtils)};
    }
    
    @Override
    public Consumer<EventBus> getDefaultsRegistrator() {
        return bus -> bus.register(new Object() {
            /**
             * The magic number that's multiplied by the scene's width/height to get the board's pref
             * width/height.
             */
            // Provide an API for overriding the default instead of hooking into ToGameEvent.Post???
            private static final double PREF_WH_FACTOR = 0.7;
    
            @Subscribe
            public void afterGameScreenLoad(ToGameEvent.Post e) {
                Pane gameRoot = (Pane) e.getGameScene().getRoot();
                Pane boardGrid = (Pane) e.getGameScene().lookup("#board");
                boardGrid.prefWidthProperty().bind(gameRoot.widthProperty().multiply(PREF_WH_FACTOR));
                boardGrid.prefHeightProperty().bind(gameRoot.heightProperty().multiply(PREF_WH_FACTOR));
            }
        });
    }
    
    // NOTE: There does not appear to be any standard human-readable computer go notation system, which is why this
    // implementation does not include one. The kifu system only really works when written by hand.
    
}