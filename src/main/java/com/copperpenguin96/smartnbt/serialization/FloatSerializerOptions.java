package com.copperpenguin96.smartnbt.serialization;

import com.copperpenguin96.smartnbt.tags.TagDef;

public class FloatSerializerOptions extends SerializerOptions {

    public boolean getUsingFractionParts() {
        Object o = _options.get("use-fraction-parts");
        if (o == null) {
            return false;
        } else {
            return (boolean)o;
        }
    }

    public void setUsingFractionParts(boolean value) {
        _options.remove("use-fraction-parts");
        _options.put("use-fraction-parts", value);
    }

    public boolean getUsingENotation() {
        Object o = _options.get("use-e");
        if (o == null) {
            return false;
        } else {
            return (boolean)o;
        }
    }

    public void setUsingUsingENotation(boolean value) {
        _options.remove("use-e");
        _options.put("use-e", value);
    }

    public FloatSerializerOptions() {
        this(false, false);
    }

    public FloatSerializerOptions(boolean useFractionParts, boolean useENotation) {
        super(TagDef.Float);

        setUsingFractionParts(useFractionParts);
        setUsingUsingENotation(useENotation);
    }
}
