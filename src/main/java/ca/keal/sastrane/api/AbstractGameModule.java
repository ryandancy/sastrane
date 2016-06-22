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
import com.google.inject.name.Names;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractGameModule<G extends Game> extends AbstractModule {
    
    private final TypeLiteral<Class<G>> G_LITERAL = new TypeLiteral<Class<G>>() {};
    
    private final Class<G> gameCls;
    private final Class<? extends Annotation> gameAnnoCls;
    
    @Override
    public void configure() {
        Multibinder<Game> gameBinder = Multibinder.newSetBinder(binder(), Game.class);
        gameBinder.addBinding().to(gameCls);
    }
    
    protected <T> void bindToInstance(String name, Class<T> cls, T t) {
        //noinspection unchecked
        MapBinder<Class<G>, T> mapBinder = MapBinder.newMapBinder(binder(), (Class<Class<G>>) gameCls.getClass(), cls,
                Names.named(name));
        mapBinder.addBinding(gameCls).toInstance(t);
        bind(cls).annotatedWith(gameAnnoCls).toInstance(t);
    }
    
    protected <T> void bindToInstance(String name, TypeLiteral<T> cls, T t) {
        MapBinder<Class<G>, T> mapBinder = MapBinder.newMapBinder(binder(), G_LITERAL, cls, Names.named(name));
        mapBinder.addBinding(gameCls).toInstance(t);
        bind(cls).annotatedWith(gameAnnoCls).toInstance(t);
    }
    
    protected <T> void bindTo(String name, Class<T> cls, Class<? extends T> impl) {
        //noinspection unchecked
        MapBinder<Class<G>, T> mapBinder = MapBinder.newMapBinder(binder(), (Class<Class<G>>) gameCls.getClass(), cls,
                Names.named(name));
        mapBinder.addBinding(gameCls).to(impl);
        bind(cls).annotatedWith(gameAnnoCls).to(impl);
    }
    
    protected <T> void bindTo(String name, TypeLiteral<T> cls, Class<? extends T> impl) {
        MapBinder<Class<G>, T> mapBinder = MapBinder.newMapBinder(binder(), G_LITERAL, cls, Names.named(name));
        mapBinder.addBinding(gameCls).to(impl);
        bind(cls).annotatedWith(gameAnnoCls).to(impl);
    }
    
}