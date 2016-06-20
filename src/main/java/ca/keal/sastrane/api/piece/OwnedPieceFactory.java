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

package ca.keal.sastrane.api.piece;

import ca.keal.sastrane.api.Player;
import com.google.common.eventbus.EventBus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;
import java.util.function.Supplier;

@Data
@RequiredArgsConstructor
public class OwnedPieceFactory {
    
    private final Function<EventBus, Piece> pieceFactory;
    private final Player player;
    
    public OwnedPieceFactory(Supplier<Piece> pieceFactory, Player player) {
        this(r -> pieceFactory.get(), player);
    }
    
    public Piece getPiece(EventBus bus) {
        return pieceFactory.apply(bus);
    }
    
}