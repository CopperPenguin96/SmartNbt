package com.copperpenguin96.smartnbt.tags;

import com.copperpenguin96.smartnbt.serialization.SerializerOptions;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class FloatTag extends NbtTag {

    public FloatTag(String name) {
        super(name);
        setPayload((byte)4);
        setID(TagDef.Float);
    }

    public FloatTag(String name, float value) {
        super(name, value);
        setPayload((byte)4);
        setID(TagDef.Float);
    }

    public FloatTag(float value) {
        super(null, value);
        setPayload((byte)4);
        setID(TagDef.Float);
    }

    public void setValue(float value) {
        super.setValue(value);
        setPayload((byte)4);
    }

    @Override
    public TagDef getID() {
        return TagDef.Float;
    }

    @Override
    public void writeToStream(OutputStream stream) {
        super.writeToStream(stream);
        try {
            ByteBuffer buffer = ByteBuffer.allocate(getPayload());
            buffer.putFloat(getFloatValue());
            stream.write(buffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return getFloatValue() + "f";
    }

    public int getWholePart() {
        return (int)getFloatValue();
    }

    public int getDecimalPart() {
        return (int)(getFloatValue() - getWholePart()) * 1000;
    }

    public String toString(SerializerOptions options) {
        if (options == null) return toString(); // return as if there are no options.
        float value = getFloatValue();
        int whole = getWholePart();
        int decimal = getDecimalPart();

        boolean useFracParts = (boolean)options.getValue("use-fraction-parts");
        boolean useENotation = (boolean)options.getValue("use-e");

        if (useENotation) {
            BigDecimal dec = new BigDecimal(value);
            return dec.toString();
        }

        if (useFracParts) {
            if (whole == 0 && decimal == 0) return "0.0f";
            if (whole == 0) {
                return "." + decimal + "f";
            }

            if (decimal == 0) {
                return whole + ".f";
            }
        }

        return toString();
    }
}
