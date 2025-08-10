package com.copperpenguin96.smartnbt.serialization;

import com.copperpenguin96.smartnbt.tags.TagDef;

import java.util.HashMap;

public abstract class SerializerOptions {
    protected final HashMap<String, Object> _options = new HashMap<>();
    private TagDef _type;

    protected SerializerOptions(TagDef type) {
        type = _type;
    }

    public boolean contains(String key) {
        return _options.containsKey(key);
    }

    public Object getValue(String key) {
        if (!contains(key)) return null;

        return _options.get(key);
    }
}
