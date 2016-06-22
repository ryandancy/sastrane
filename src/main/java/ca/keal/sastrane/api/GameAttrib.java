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

package ca.keal.sastrane.api;

/** The attributes for {@link GameAttribute @GameAttribute}. GAAAH NAMING TROUBLES GAAAAH */
public enum GameAttrib {
    
    NAME, PACKAGE, RESOURCE_BUNDLE_NAME, I18N_NAME, ICON, CSS, PLAYERS, AI, BOARD_FACTORY, PLACING_PIECES,
    ARBITRATOR, NOTATER;
    
    public static GameAttribute attribute(GameAttrib value) {
        return new GameAttributeImpl(value);
    }
    
}