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
import com.google.inject.multibindings.Multibinder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractGameModule extends AbstractModule {
    
    private final Class<? extends Game> infoCls;
    private final Class<? extends Annotation> gameAnnoCls;
    
    @Override
    public void configure() {
        Multibinder<Game> gameBinder = Multibinder.newSetBinder(binder(), Game.class);
        gameBinder.addBinding().to(infoCls);
    }
    
    protected <T> void bindToInstance(Class<T> cls, T t) {
        bind(cls).annotatedWith(gameAnnoCls).toInstance(t);
    }
    
    protected <T> void bindToInstance(TypeLiteral<T> cls, T t) {
        bind(cls).annotatedWith(gameAnnoCls).toInstance(t);
    }
    
    protected <T> void bindTo(Class<T> cls, Class<? extends T> impl) {
        bind(cls).annotatedWith(gameAnnoCls).to(impl);
    }
    
    protected <T> void bindTo(TypeLiteral<T> cls, Class<? extends T> impl) {
        bind(cls).annotatedWith(gameAnnoCls).to(impl);
    }
    
}