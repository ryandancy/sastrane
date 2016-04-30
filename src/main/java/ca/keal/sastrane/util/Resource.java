package ca.keal.sastrane.util;

import lombok.Data;
import lombok.NonNull;

import java.net.URL;

@Data
public class Resource {
    
    @NonNull
    private final String pkg;
    
    @NonNull
    private final String filename;
    
    public URL get() {
        return ClassLoader.getSystemResource(getFilename());
    }
    
    @NonNull
    public String getFilename() {
        return toString();
    }
    
    @Override
    @NonNull
    public String toString() {
        return pkg.replace('.', '/') + "/" + filename;
    }
    
    @Data
    public static class Unmangled {
        
        private final String pkg;
        private final String unmangled;
        private final String ext;
        
        public Unmangled(@NonNull String pkg, @NonNull String unmangled) {
            this.pkg = pkg;
            if (unmangled.lastIndexOf('.') == -1) {
                this.unmangled = unmangled;
                ext = "";
            } else {
                this.unmangled = unmangled.substring(0, unmangled.lastIndexOf('.'));
                ext = unmangled.substring(unmangled.lastIndexOf('.'));
            }
        }
        
        public Resource mangle(@NonNull String mangler) {
            return new Resource(pkg, unmangled + "_" + mangler + ext);
        }
        
    }
    
}