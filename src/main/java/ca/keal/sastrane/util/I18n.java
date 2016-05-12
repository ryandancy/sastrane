package ca.keal.sastrane.util;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public final class I18n {
    
    private static final List<String> baseNames = new ArrayList<>();
    private static final List<ResourceBundle> bundles = new ArrayList<>();
    
    @Getter private static Locale locale = new Locale("en_US");
    
    public static void setLocale(Locale locale) {
        I18n.locale = locale;
        
        if (bundles.size() > 0) {
            bundles.clear();
        }
    }
    
    public static void load(String baseName) {
        baseNames.add(baseName);
        
        if (bundles.size() > 0) {
            bundles.clear();
        }
    }
    
    public static String localize(String key, Object... formatArgs) {
        if (bundles.size() == 0) {
            createBundles();
        }
        
        for (ResourceBundle bundle : bundles) {
            try {
                return String.format(bundle.getString(key), formatArgs);
            } catch (MissingResourceException ignored) {}
        }
        
        return key;
    }
    
    public static ResourceBundle getBundle() {
        if (bundles.size() == 0) {
            createBundles();
        }
        
        return new ListResourceBundle() {
            @Override
            protected Object[][] getContents() {
                List<Object[]> contents = new ArrayList<>();
                
                for (ResourceBundle bundle : bundles) {
                    for (String key : bundle.keySet()) {
                        contents.add(new Object[] {key, bundle.getObject(key)});
                    }
                }
                
                return contents.toArray(new Object[0][]);
            }
        };
    }
    
    private static void createBundles() {
        bundles.addAll(baseNames.stream()
                .map(baseName -> ResourceBundle.getBundle(baseName, getLocale()))
                .collect(Collectors.toList()));
    }
    
}