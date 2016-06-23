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

import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.annotation.Nullable;

/** Internal utility class for AbstractGameModule and GameAttr */
@Getter
@EqualsAndHashCode
class PossiblyTypedValue<T> {
    
    @Nullable private Class<T> cls = null;
    @Nullable private TypeLiteral<T> literal = null;
    @Nullable private Class<? extends T> valueCls = null;
    @Nullable private T value = null;
    @Nullable private Provider<T> valueProvider = null;
    
    PossiblyTypedValue(Class<T> cls, T value) {
        this.cls = cls;
        this.value = value;
    }
    
    PossiblyTypedValue(Class<T> cls, Class<? extends T> valueCls) {
        this.cls = cls;
        this.valueCls = valueCls;
    }
    
    PossiblyTypedValue(Class<T> cls, Provider<T> valueProvider) {
        this.cls = cls;
        this.valueProvider = valueProvider;
    }
    
    @SuppressWarnings("unchecked")
    PossiblyTypedValue(T value) {
        this((Class<T>) value.getClass(), value);
    }
    
    PossiblyTypedValue(TypeLiteral<T> literal, T value) {
        this.literal = literal;
        this.value = value;
    }
    
    PossiblyTypedValue(TypeLiteral<T> literal, Class<? extends T> valueCls) {
        this.literal = literal;
        this.valueCls = valueCls;
    }
    
    PossiblyTypedValue(TypeLiteral<T> literal, Provider<T> valueProvider) {
        this.literal = literal;
        this.valueProvider = valueProvider;
    }
    
    @Override
    public String toString() {
        if (value != null) {
            return value.toString();
        } else if (valueCls != null) {
            return valueCls.toString();
        } else if (valueProvider != null) {
            return valueProvider.toString();
        } else {
            return "null";
        }
    }
    
}