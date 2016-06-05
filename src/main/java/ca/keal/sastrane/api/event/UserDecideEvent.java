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

package ca.keal.sastrane.api.event;

import ca.keal.sastrane.api.Decision;
import ca.keal.sastrane.api.Round;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Posted when the user decides a {@link ca.keal.sastrane.api.Decision}.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDecideEvent extends TurnEvent {
    
    private Decision decision;
    
    public UserDecideEvent(Round round, Decision decision) {
        super(round);
        this.decision = decision;
    }
    
}