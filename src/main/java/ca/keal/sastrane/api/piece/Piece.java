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
import ca.keal.sastrane.util.Resource;

public interface Piece {
    
    /**
     * <strong>Excludes file extension!!!</strong> File extension is assumed to be .png; it + the player's name will be
     * mangled together to form the final filename. For example a piece that returns {@code ("com.foo", "bar")} in this
     * method, and whose player's {@link Player#getName()} method returns {@code "baz"}, will have a final image
     * filename of {@code "com/foo/bar_baz.png"}.
     *
     * @return (packageName, filename) of the piece's icon, with the file name <strong>without the extension!</strong>
     */
    Resource.Unmangled getImage();
    
}