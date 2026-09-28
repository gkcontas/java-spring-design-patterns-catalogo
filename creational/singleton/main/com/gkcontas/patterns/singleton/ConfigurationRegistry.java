package com.gkcontas.patterns.singleton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton as an enum, which is the only form worth writing by hand in Java.
 *
 * <p>The textbook version — a private constructor, a static field and a null check — is
 * subtly wrong in three ways at once: it needs explicit synchronisation to be safe under
 * concurrent first access, it can be defeated by reflection calling the private
 * constructor, and deserialising an instance produces a second one. A single-element enum
 * gets all three from the language: the JVM guarantees one instance per class loader,
 * reflection refuses to instantiate an enum, and serialisation is defined to return the
 * existing constant.
 */
public enum ConfigurationRegistry {

    INSTANCE;

    private final Map<String, String> settings = new ConcurrentHashMap<>();

    public void set(String key, String value) {
        settings.put(key, value);
    }

    public String get(String key, String defaultValue) {
        return settings.getOrDefault(key, defaultValue);
    }

    public int size() {
        return settings.size();
    }

    public void clear() {
        settings.clear();
    }
}
