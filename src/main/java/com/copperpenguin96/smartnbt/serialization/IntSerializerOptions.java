package com.copperpenguin96.smartnbt.serialization;

import com.copperpenguin96.smartnbt.tags.TagDef;

public class IntSerializerOptions extends SerializerOptions {

    public boolean getUsingHex() {
        Object o = _options.get("use-hex");
        if (o == null) {
            return false;
        } else {
            return (boolean)o;
        }
    }

    public void setUsingHex(boolean value) {
        _options.remove("use-hex");
        _options.put("use-hex", value);
    }

    public IntSerializerOptions() {
        this(false);
    }

    public IntSerializerOptions(boolean useFractionParts) {
        super(TagDef.Int);

        setUsingHex(useFractionParts);
    }
}
