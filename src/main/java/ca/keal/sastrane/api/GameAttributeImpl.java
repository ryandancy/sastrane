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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.lang.annotation.Annotation;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
@SuppressWarnings("ClassExplicitlyAnnotation")
class GameAttributeImpl implements GameAttribute {
    
    private final GameAttr value;
    
    @Override
    public Class<? extends Annotation> annotationType() {
        return GameAttribute.class;
    }
    
    // Implementations for toString(), equals(), hashCode() are as specified in Annotation
    
    @Override
    public String toString() {
        return "@" + GameAttribute.class.getName() + "(value=" + value + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        return o instanceof GameAttribute && value.equals(((GameAttribute) o).value());
    }
    
    @Override
    public int hashCode() {
        return (127 * "value".hashCode()) ^ value.hashCode();
    }
    
}