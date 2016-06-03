package ca.keal.sastrane.util;

import lombok.Data;
import lombok.SneakyThrows;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

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
    public static Resource[] getAllFromFile(Resource file) {
        return new BufferedReader(new InputStreamReader(file.get().openStream()))
                .lines()
                .map(String::trim)
                .filter(s -> !s.isEmpty() && !s.startsWith("#"))
                .map(s -> s.split("/"))
                .map(res -> res.length == 2 ? res : new String[] {file.getPkg(), res[1]})
                .map(res -> new Resource(res[0], res[1]))
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