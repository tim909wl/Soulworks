package de.soulworks.debug;

import de.soulworks.error.SoulworksError;

import java.util.HashMap;
import java.util.Map;

public class Debug {
    private static Map<String, Object> debugItems = new HashMap<>();

    public static void setItem(String key, Object value) {
        debugItems.put(key, value);
    }

    public static Object getItem(String key) {
        return debugItems.get(key);
    }

    public static void updateItem(String key, Object value) throws SoulworksError {
        if (debugItems.containsKey(key)) {
            debugItems.put(key, value);
        } else {
            throw new SoulworksError("Item with key '" + key + "' does not exist in the debug items.", SoulworksError.ErrorType.DEBUG);
        }
    }

    public static void removeItem(String key) {
        debugItems.remove(key);
    }

    public static void clearItems() {
        debugItems.clear();
    }
}

