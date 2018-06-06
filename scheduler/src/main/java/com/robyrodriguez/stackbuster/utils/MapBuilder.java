package com.robyrodriguez.stackbuster.utils;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder {
    private Map<String, Object> result = new HashMap<>();

    public MapBuilder add(String key, Object value) {
        result.put(key, value);
        return this;
    }

    public Map<String, Object> build() {
        return result;
    }
}
