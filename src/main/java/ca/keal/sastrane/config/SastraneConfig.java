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

package ca.keal.sastrane.config;

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Converter;
import org.aeonbits.owner.Mutable;

import java.lang.reflect.Method;
import java.util.Locale;

@Sources("file:" + SastraneConfig.FILE_PATH)
public interface SastraneConfig extends Config, Accessible, Mutable {
    
    String FILE_PATH = "config.properties";
    
    String SOUNDFX_VOLUME_KEY = "volume.soundfx";
    
    @Key(SOUNDFX_VOLUME_KEY)
    @DefaultValue("0.5")
    double soundFXVolume();
    
    String MUSIC_VOLUME_KEY = "volume.music";
    
    @Key(MUSIC_VOLUME_KEY)
    @DefaultValue("0.5")
    double musicVolume();
    
    String LOCALE_KEY = "locale";
    
    @Key(LOCALE_KEY)
    @DefaultValue("en-US")
    @ConverterClass(LocaleConverter.class)
    Locale locale();
    
    /** Simple wrapper over {@link Locale#forLanguageTag(String)} as a {@link Converter} for OWNER. */
    class LocaleConverter implements Converter<Locale> {
        @Override
        public Locale convert(Method method, String s) {
            return Locale.forLanguageTag(s);
        }
    }
    
}