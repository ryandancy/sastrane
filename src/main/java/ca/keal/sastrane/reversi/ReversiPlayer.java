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

import ca.keal.sastrane.api.Player;
import ca.keal.sastrane.api.Square;
import ca.keal.sastrane.util.Resource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
enum ReversiPlayer implements Player {
    
    BLACK("black", "reversi.player.black", new Resource("ca.keal.sastrane.reversi", "disk_black.png")),
    WHITE("white", "reversi.player.white", new Resource("ca.keal.sastrane.reversi", "disk_white.png"));
    
    private final String name;
    private final String i18nName;
    private final Resource icon;
    
    @Override
    public Square perspectivize(Square pos, Square original) {
        return pos;
    }
    
}
