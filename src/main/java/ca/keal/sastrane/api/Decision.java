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

import ca.keal.sastrane.util.Resource;

/**
 * An interface representing a decision, such as a pawn promotion in chess.
 * <p>
 * This interface is designed to be used as an <a href="http://stackoverflow.com/a/2709831">extensible enum</a>.
 */
public interface Decision {
    
    void onChoose(Round round);
    
    default Round whatIf(Round round) {
        Round newRound = new Round(round);
        onChoose(newRound);
        return newRound;
    }
    
    Resource getIcon();
    
    String getI18nName();
    
}