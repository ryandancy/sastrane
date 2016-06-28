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

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryProvider;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

// A WARNING TO ALL YE WHO DARE ENTER: here be the dragons of GENERICS HELL, forged from the EVILS of type erasure
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@ToString
public abstract class AbstractGameModule extends AbstractModule {
    
    /** A unique string that identifies the game; usually a package name */
    private final String id;
    
    private final Map<GameAttr, PossiblyTypedValue<?>> attribsToValues = new HashMap<>();
    
    @Override
    public void configure() {
        Multibinder<String> idBinder = Multibinder.newSetBinder(binder(), String.class);
        idBinder.addBinding().toInstance(id);

        GameAttr.autoAddAllPossible(attribsToValues);
        GameAttr.fillInDefaults(attribsToValues);

        attribsToValues.forEach((attrib, value) -> {
            if (value.getLiteral() != null) {
                doLiteralBinding(attrib, value);
            } else {
                doClassBinding(attrib, value);
            }
        });
    }
    
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private <T> void doClassBinding(GameAttr attrib, PossiblyTypedValue<T> ptv) {
        MapBinder<String, T> mapBinder = MapBinder.newMapBinder(binder(), String.class, ptv.getCls(),
                GameAttr.attribute(attrib));
        completeBinding(mapBinder, ptv);
    }
    
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private <T> void doLiteralBinding(GameAttr attrib, PossiblyTypedValue<T> ptv) {
        MapBinder<String, T> mapBinder = MapBinder.newMapBinder(binder(), TypeLiteral.get(String.class),
                ptv.getLiteral(), GameAttr.attribute(attrib));
        completeBinding(mapBinder, ptv);
    }
    
    private <T> void completeBinding(MapBinder<String, T> mapBinder, PossiblyTypedValue<T> ptv) {
        mapBinder.permitDuplicates();
        if (ptv.getValueProvider() != null) {
            mapBinder.addBinding(id).toProvider(ptv.getValueProvider());
        } else if (ptv.getValueCls() != null) {
            mapBinder.addBinding(id).to(ptv.getValueCls());
        } else {
            mapBinder.addBinding(id).toInstance(ptv.getValue());
        }
    }
    
    protected <T> void bindToInstance(GameAttr attrib, Class<T> cls, T t) {
        attribsToValues.put(attrib, new PossiblyTypedValue<>(cls, t));
    }
    
    protected <T> void bindToInstance(GameAttr attrib, TypeLiteral<T> cls, T t) {
        attribsToValues.put(attrib, new PossiblyTypedValue<>(cls, t));
    }
    
    protected <T> void bindTo(GameAttr attrib, Class<T> cls, Class<? extends T> impl) {
        attribsToValues.put(attrib, new PossiblyTypedValue<>(cls, impl));
    }
    
    protected <T> void bindTo(GameAttr attrib, TypeLiteral<T> cls, Class<? extends T> impl) {
        attribsToValues.put(attrib, new PossiblyTypedValue<>(cls, impl));
    }
    
    protected <T> void bindToProvider(GameAttr attrib, Class<T> cls, Provider<T> provider) {
        attribsToValues.put(attrib, new PossiblyTypedValue<>(cls, provider));
    }
    
    protected <T> void bindToProvider(GameAttr attrib, TypeLiteral<T> cls, Provider<T> provider) {
        attribsToValues.put(attrib, new PossiblyTypedValue<>(cls, provider));
    }
    
    @SuppressWarnings("deprecation")
    protected <F> void installFactory(GameAttr attrib, Class<F> factoryCls, Class<?> impl) {
        // We use the deprecated FactoryProvider because it's a provider - FactoryModuleBuilder wouldn't work
        bindToProvider(attrib, factoryCls, FactoryProvider.newFactory(factoryCls, impl));
    }
    
}