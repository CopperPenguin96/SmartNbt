package com.copperpenguin96.smartnbt.tags;

import java.io.IOException;
import java.io.OutputStream;

public class EndTag extends NbtTag {

    public EndTag() {
        super(null);
        setPayload((byte)0);
        setID(TagDef.End);
    }

    @Override
    public TagDef getID() {
        return TagDef.End;
    }

    @Override
    public void writeToStream(OutputStream stream) {
        super.writeToStream(stream);
        try {
            stream.write(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
