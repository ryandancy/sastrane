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
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.util.Types;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractGameModule<G extends Game> extends AbstractModule {
    
    private final Class<G> gameCls;
    private final Class<? extends Annotation> gameAnnoCls;
    
    @Getter(lazy = true)
    @SuppressWarnings("unchecked")
    private final TypeLiteral<Class<G>> gLiteral = (TypeLiteral<Class<G>>) TypeLiteral.get(
            Types.newParameterizedType(Class.class, gameCls)); // because generic type erasure is stupid
    
    @Override
    public void configure() {
        Multibinder<Game> gameBinder = Multibinder.newSetBinder(binder(), Game.class);
        gameBinder.addBinding().to(gameCls);
    }
    
    protected <T> void bindToInstance(GameAttrib attrib, Class<T> cls, T t) {
        //noinspection unchecked
        MapBinder<Class<G>, T> mapBinder = MapBinder.newMapBinder(binder(), (Class<Class<G>>) gameCls.getClass(), cls,
                GameAttrib.attribute(attrib));
        mapBinder.addBinding(gameCls).toInstance(t);
        bind(cls).annotatedWith(gameAnnoCls).toInstance(t);
    }
    
    protected <T> void bindToInstance(GameAttrib attrib, TypeLiteral<T> cls, T t) {
        MapBinder<Class<G>, T> mapBinder = MapBinder.newMapBinder(binder(), getGLiteral(), cls,
                GameAttrib.attribute(attrib));
        mapBinder.addBinding(gameCls).toInstance(t);
        bind(cls).annotatedWith(gameAnnoCls).toInstance(t);
    }
    
    protected <T> void bindTo(GameAttrib attrib, Class<T> cls, Class<? extends T> impl) {
        //noinspection unchecked
        MapBinder<Class<G>, T> mapBinder = MapBinder.newMapBinder(binder(), (Class<Class<G>>) gameCls.getClass(), cls,
                GameAttrib.attribute(attrib));
        mapBinder.addBinding(gameCls).to(impl);
        bind(cls).annotatedWith(gameAnnoCls).to(impl);
    }
    
    protected <T> void bindTo(GameAttrib attrib, TypeLiteral<T> cls, Class<? extends T> impl) {
        MapBinder<Class<G>, T> mapBinder = MapBinder.newMapBinder(binder(), getGLiteral(), cls,
                GameAttrib.attribute(attrib));
        mapBinder.addBinding(gameCls).to(impl);
        bind(cls).annotatedWith(gameAnnoCls).to(impl);
    }
    
}