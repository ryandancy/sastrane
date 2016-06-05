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

import javafx.scene.Scene;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Posted when a game-specific scene transition occurs.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NewSceneEvent extends GuiEvent {
    
    private final Scene previousScene;
    
    public NewSceneEvent(Scene previousScene, Scene nextScene) {
        super(nextScene);
        this.previousScene = previousScene;
    }
    
    public Scene getNextScene() {
        return getScene();
    }
    
}