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

import com.google.common.collect.HashMultiset;
import com.google.inject.assistedinject.FactoryProvider;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@EqualsAndHashCode
@ToString
public final class Utils {
    
    public static final Random RANDOM = new Random();
    
    private Utils() {}
    
    public static File openOrCreateFile(String path) {
        try {
            File file = new File(path);
            if (!file.isFile() && !file.createNewFile()) {
                throw new IOException("Cannot create file " + file.getAbsolutePath());
            }
            return file;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    @SafeVarargs
    public static <T> T randomChoice(T... array) {
        return array[RANDOM.nextInt(array.length)];
    }
    
    /**
     * Returns whether the two collections have equal elements; that is, the same number of elements, and all elements
     * have an equal counterpart on the other set. Disregards order.
     */
    public static <E> boolean areElementsEqual(Collection<E> a, Collection<E> b) {
        return HashMultiset.create(a).equals(HashMultiset.create(b));
    }
    
    /**
     * Returns the list with only the specified indexes, all the others filtered out.
     */
    public static <E> List<E> selectIndexes(List<E> list, int... indexes) {
        List<E> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            // this is what we have to write to get a simple contains on an array
            for (int idx : indexes) {
                if (i == idx) {
                    newList.add(list.get(i));
                    break;
                }
            }
        }
        return newList;
    }
    
    /** Returns factoryCls instantiated with implCls */
    @SuppressWarnings("deprecation")
    public static <F, I> F instantiateFactory(Class<F> factoryCls, Class<I> implCls) {
        // Horrible, no-good, very bad hack using deprecated APIs
        return FactoryProvider.newFactory(factoryCls, implCls).get();
    }
    
}