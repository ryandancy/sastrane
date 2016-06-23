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

import com.google.inject.Singleton;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Singleton
public class I18n {
    
    private final List<String> baseNames = new ArrayList<>();
    private final List<ResourceBundle> bundles = new ArrayList<>();
    
    @Getter private Locale locale;
    
    public void setLocale(Locale locale) {
        this.locale = locale;
        
        if (bundles.size() > 0) {
            bundles.clear();
        }
    }
    
    public void load(String baseName) {
        baseNames.add(baseName);
        
        if (bundles.size() > 0) {
            bundles.clear();
        }
    }
    
    public String localize(String key, Object... formatArgs) {
        if (bundles.size() == 0) {
            createBundles();
        }
        
        for (ResourceBundle bundle : bundles) {
            try {
                return String.format(bundle.getString(key), formatArgs);
            } catch (MissingResourceException ignored) {}
        }
        
        return key;
    }
    
    public ResourceBundle getBundle() {
        if (bundles.size() == 0) {
            createBundles();
        }
        
        return new ListResourceBundle() {
            @Override
            protected Object[][] getContents() {
                List<Object[]> contents = new ArrayList<>();
                
                for (ResourceBundle bundle : bundles) {
                    for (String key : bundle.keySet()) {
                        contents.add(new Object[] {key, bundle.getObject(key)});
                    }
                }
                
                return contents.toArray(new Object[0][]);
            }
        };
    }
    
    private void createBundles() {
        bundles.addAll(baseNames.stream()
                .map(baseName -> ResourceBundle.getBundle(baseName, getLocale()))
                .collect(Collectors.toList()));
    }
    
}