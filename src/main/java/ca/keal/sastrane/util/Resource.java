package ca.keal.sastrane.util;

import lombok.Data;

import javax.annotation.Nullable;
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
    
    @Override
    public String toString() {
        return pkg.replace('.', '/') + "/" + filename;
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