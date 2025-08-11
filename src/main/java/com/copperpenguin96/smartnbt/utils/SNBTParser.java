package com.copperpenguin96.smartnbt.utils;

import com.copperpenguin96.smartnbt.NbtFormatException;
import com.copperpenguin96.smartnbt.tags.CompoundTag;
import com.copperpenguin96.smartnbt.tags.StringTag;

public class SNBTParser {
    private String _raw;

    public String getRaw() {
        return _raw;
    }

    public SNBTParser(String raw) {
        _raw = raw;
    }

    public static CompoundTag read(String snbt) throws NbtFormatException {
        return new SNBTParser(snbt).read();
    }

    public CompoundTag read() throws NbtFormatException {
        String base = _raw.trim();
        char[] chars = base.toCharArray();
        if (chars[0] != '{') throw new NbtFormatException("Malformed SNBT. Did not read start.");
        if (chars[base.length() -1] != '}') throw new NbtFormatException("Malformed SNBT. Did not read end.");

        String innerString = base.substring(1, base.length() - 1);
        // todo use StringReader to read sectioned parts
        CompoundTag returnTag = new CompoundTag();
        String[] sets = innerString.split(",(?=(?:(?:[^\"]*\"){2})*[^\"]*$)");

        for (String set : sets) {
            String[] keyValuePair = set.split(":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            String key = keyValuePair[0];
            String value = keyValuePair[1];
            String tagName = "";
            StringReader keyReader = new StringReader(key);

            if (keyReader.peek() == '"') {
                tagName = keyReader.readQuotedString();
            } else {
                tagName = keyReader.readUnquotedString();
            }

            StringReader valueReader = new StringReader(value);

            if (valueReader.peek() == '"') {
                StringTag strTag = new StringTag(tagName, valueReader.readQuotedString());
                returnTag.add(strTag);
            } else {
                
            }
        }

        return returnTag;
    }

}
