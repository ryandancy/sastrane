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

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Cleanup;
import lombok.Data;
import lombok.SneakyThrows;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Data
public class Resource {
    
    private final String pkg;
    
    private final String filename;
    
    public URL get() {
        URL url = getNullable();
        if (url == null) throw new NullPointerException(getFilename() + " not found");
        return url;
    }
    
    @Nullable
    public URL getNullable() {
        return ClassLoader.getSystemResource(getFilename());
    }
    
    public String getFilename() {
        return toString();
    }
    
    @SneakyThrows
    public String getFullFilename() {
        return get().toExternalForm();
    }
    
    @Override
    public String toString() {
        return pkg.replace('.', '/') + "/" + filename;
    }
    
    /**
     * {@code file} should be a text file with newlines separating resources. Each resource in the file is an optional
     * package (if missing is assumed to be {@code file}'s package), a '/' (forwards slash), and the filename. Comments
     * are lines <b>starting</b> with '#'. Each line is trimmed.
     * <p>
     * e.g., assuming this file is in package {@code com.foo.bar}:
     * <code><pre>
     *     # A list of resources
     *     com.foo.baz/abc.xyz
     *     asdjke.html
     * </pre></code>
     * returns {@code [Resource(pkg=com.foo.baz,filename=abc.xyz),Resource(pkg=com.foo.bar,filename=asdjke.html)]}.
     */
    @SneakyThrows
    @SuppressFBWarnings("OS_OPEN_STREAM")
    public static Resource[] getAllFromFile(Resource file) {
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(file.get().openStream(),
                StandardCharsets.UTF_8));
        return reader
                .lines()
                .map(String::trim)
                .filter(s -> !s.isEmpty() && !s.startsWith("#"))
                .map(Splitter.on('/').trimResults()::split)
                .map(Lists::newArrayList)
                .peek(res -> { if (res.size() == 1) res.add(0, file.getPkg()); })
                .map(res -> new Resource(res.get(0), res.get(1)))
                .toArray(Resource[]::new);
    }
    
    @Data
    public static class Unmangled {
        
        private final String pkg;
        private final String unmangled;
        private final String ext;
        
        public Unmangled(String pkg, String unmangled) {
            this.pkg = pkg;
            if (unmangled.lastIndexOf('.') == -1) {
                this.unmangled = unmangled;
                ext = "";
            } else {
                this.unmangled = unmangled.substring(0, unmangled.lastIndexOf('.'));
                ext = unmangled.substring(unmangled.lastIndexOf('.'));
            }
        }
        
        public Resource mangle(String mangler) {
            return new Resource(pkg, unmangled + "_" + mangler + ext);
        }
        
    }
    
}