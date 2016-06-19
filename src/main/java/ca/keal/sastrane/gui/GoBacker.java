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

package ca.keal.sastrane.gui;

import ca.keal.sastrane.util.Resource;
import javafx.event.Event;
import javafx.fxml.FXML;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

/**
 * A controller with the ability to go back to a previous scene.
 */
@AllArgsConstructor
abstract class GoBacker {
    
    protected Resource previous;
    
    @FXML
    @SneakyThrows
    protected void goBack(Event e) {
        GuiUtils.goTo(previous);
    }
    
}