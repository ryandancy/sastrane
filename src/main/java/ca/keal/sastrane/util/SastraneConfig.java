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

package ca.keal.sastrane.util;

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Mutable;

@Sources("file:config.properties")
public interface SastraneConfig extends Config, Accessible, Mutable {
    
    String SOUNDFX_VOLUME_KEY = "volume.soundfx";
    
    @Key(SOUNDFX_VOLUME_KEY)
    @DefaultValue("0.5")
    double soundFXVolume();
    
    String MUSIC_VOLUME_KEY = "volume.music";
    
    @Key(MUSIC_VOLUME_KEY)
    @DefaultValue("0.5")
    double musicVolume();
    
}