package com.copperpenguin96.smartnbt.serialization;

import com.copperpenguin96.smartnbt.tags.TagDef;

public class FloatSerializerOptions extends SerializerOptions {

    public boolean getUsingFractionParts() {
        return (boolean)_options.get("use-fraction-parts");
    }

    public void setUsingFractionParts(boolean value) {
        _options.remove("use-fraction-parts");
        _options.put("use-fraction-parts", value);
    }

    public boolean getUsingENotation() {
        return (boolean)_options.get("use-e");
    }

    public void setUsingUsingENotation(boolean value) {
        _options.remove("use-e");
        _options.put("use-e", value);
    }

    public boolean getUseUnderscore() {
        return (boolean)_options.get("use-underscore");
    }

    public void setUseUnderscore(boolean value) {
        _options.remove("use-underscore");
        _options.put("use-underscore", value);
    }

    public FloatSerializerOptions() {
        this(false, false, false);
    }

    public FloatSerializerOptions(boolean useFractionParts, boolean useENotation, boolean useUnderscore) {
        super(TagDef.Float);

        setUsingFractionParts(useFractionParts);
        setUsingUsingENotation(useENotation);
    }
}
