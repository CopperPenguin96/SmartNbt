package com.copperpenguin96.smartnbt.utils;

import com.copperpenguin96.smartnbt.NbtFormatException;
import com.copperpenguin96.smartnbt.tags.*;

import java.io.InvalidObjectException;
import java.util.*;

public class SNBTParser {
    private String _raw;

    public String getRaw() {
        return _raw;
    }

    public SNBTParser(String raw) {
        _raw = raw;
    }

    public static CompoundTag read(String snbt) throws NbtFormatException, InvalidObjectException {
        return new SNBTParser(snbt).parseCompound();
    }

    public CompoundTag parseCompound() throws NbtFormatException, InvalidObjectException {
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

            if (valueReader.peek() == '"') { // must assume it's a string tag
                StringTag stringTag = new StringTag(tagName, valueReader.readQuotedString());
                returnTag.add(stringTag);
            } else if (valueReader.isByte()) {
                ByteTag byteTag = new ByteTag(tagName, (byte)valueReader.readInt());
                returnTag.add(byteTag);
            } else if (valueReader.isShort()) {
                ShortTag shortTag = new ShortTag(tagName, (short)valueReader.readInt());
                returnTag.add(shortTag);
            } else if (valueReader.isInt()) {
                IntTag intTag = new IntTag(tagName, valueReader.readInt());
                returnTag.add(intTag);
            } else if (valueReader.isLong()) {
                LongTag longTag = new LongTag(tagName, valueReader.readLong());
                returnTag.add(longTag);
            } else if (valueReader.isFloat()) {
                FloatTag floatTag = new FloatTag(tagName, valueReader.readFloat());
                returnTag.add(floatTag);
            } else if (valueReader.isDouble()) {
                DoubleTag doubleTag = new DoubleTag(tagName, valueReader.readDouble());
                returnTag.add(doubleTag);
            } else { // not a string or a number. check for boolean, lists, compounds, and number arrays
                String rest = valueReader.peekRest().trim(); // no need to not trim, it's not a string
                char[] restChars = rest.toCharArray();
                // check if boolean
                if (rest.equalsIgnoreCase("false") || rest.equalsIgnoreCase("true")) {
                    BoolTag boolTag = new BoolTag(tagName, Boolean.parseBoolean(rest));
                    returnTag.add(boolTag);
                }

                // check if valid compound
                if (restChars[0] == '{') {
                    if (restChars[restChars.length - 1] == '}') {
                        CompoundTag compoundTag = new CompoundTag(SNBTParser.read(rest));
                        returnTag.add(tagName, compoundTag);
                    } else {
                        throw new NbtFormatException("EOF on Compound expected and not found.");
                    }
                }

                String restNoWhite = rest.replaceAll("\\s", "");
                // check if valid list or array
                if (restChars[0] == '[') {
                    if (restChars[restChars.length - 1] == ']') {
                        // prepare to oganize and read
                        String valueOnly = restNoWhite.substring(3, restNoWhite.length() - 1);
                        String[] parts = valueOnly.split(",");
                        StringReader partReader;

                        // check definition of array type or if such definition exists.
                        String defi = restNoWhite.substring(1, 3);
                        switch (defi) {
                            case "B;": // handling a supposed byte array
                                ArrayList<Byte> byteArray = new ArrayList<>();
                                for (String part : parts) {
                                    partReader = new StringReader(part);
                                    if (partReader.isByte()) {
                                        byteArray.add((byte)partReader.readInt());
                                    } else {
                                        throw new NbtFormatException("Non-Bytes in Byte Array.");
                                    }
                                }

                                ByteArrayTag byteArrayTag = new ByteArrayTag(tagName, byteArray);
                                returnTag.add(byteArrayTag);
                                break;
                            case "I;": // handling a supposed int array
                                ArrayList<Integer> intArray = new ArrayList<>();
                                for (String part : parts) {
                                    partReader = new StringReader(part);
                                    if (partReader.isInt()) {
                                        intArray.add(partReader.readInt());
                                    } else {
                                        throw new NbtFormatException("Non-Integers in Int Array");
                                    }
                                }

                                IntArrayTag intArrayTag = new IntArrayTag(tagName, intArray);
                                returnTag.add(intArrayTag);
                                break;
                            case "L;": // handling a supposed long array
                                ArrayList<Long> longArray = new ArrayList<>();
                                for (String part : parts) {
                                    partReader = new StringReader(part);
                                    if (partReader.isLong()) {
                                        longArray.add(partReader.readLong());
                                    } else {
                                        throw new NbtFormatException("Non-Longs in Long Array");
                                    }
                                }

                                LongArrayTag longArrayTag = new LongArrayTag(tagName, longArray);
                                returnTag.add(longArrayTag);
                                break;
                            default: // does not match applicable array types, must assume it is a list.
                                ListTag listTag = (ListTag) parseList(rest);
                                returnTag.add(tagName, listTag);
                        }

                    } else {
                        throw new NbtFormatException("EOF on List/Array expected and not found.");
                    }
                }
            }
        }

        return returnTag;
    }

    private int _listLevel = 0;
    private final int _maxLevel = 512;

    /**
     * Separate method created to handle recursive lists
     */
    public NbtTag parseList(String value) throws NbtFormatException, InvalidObjectException {
        if (_listLevel > _maxLevel) {
            throw new NbtFormatException("Too many nested lists.");
        }

        String[] parts = value.split(",(?=(?:(?:[^\"]*\"){2})*[^\"]*$)");

        ArrayList<TagDef> tagTypes = new ArrayList<>();
        ArrayList<NbtTag> tags = new ArrayList<>();

        for (String part : parts) {
            StringReader partReader = new StringReader(part);
            if (partReader.peek() == '"') {
                tagTypes.add(TagDef.String);
                StringTag stringTag = new StringTag();
                stringTag.setValue(partReader.readQuotedString());
                tags.add(stringTag);
            } else if (partReader.isByte()) {
                tagTypes.add(TagDef.Byte);
                ByteTag byteTag = new ByteTag((byte)partReader.readInt());
                tags.add(byteTag);
            } else if (partReader.isShort()) {
                tagTypes.add(TagDef.Short);
                ShortTag shortTag = new ShortTag((short)partReader.readInt());
                tags.add(shortTag);
            } else if (partReader.isInt()) {
                tagTypes.add(TagDef.Int);
                IntTag intTag = new IntTag(partReader.readInt());
                tags.add(intTag);
            } else if (partReader.isLong()) {
                tagTypes.add(TagDef.Long);
                LongTag longTag = new LongTag(partReader.readLong());
                tags.add(longTag);
            } else if (partReader.isFloat()) {
                tagTypes.add(TagDef.Float);
                FloatTag floatTag = new FloatTag(partReader.readFloat());
                tags.add(floatTag);
            } else if (partReader.isDouble()) {
                tagTypes.add(TagDef.Double);
                DoubleTag doubleTag = new DoubleTag(partReader.readDouble());
                tags.add(doubleTag);
            } else { // not a string or a number. check for boolean, lists, compounds, and number arrays
                String rest = partReader.peekRest().trim(); // no need to not trim, it's not a string
                char[] restChars = rest.toCharArray();
                // check if boolean
                if (rest.equalsIgnoreCase("false") || rest.equalsIgnoreCase("true")) {
                    tagTypes.add(TagDef.Boolean);
                    BoolTag boolTag = new BoolTag(Boolean.parseBoolean(rest));
                    tags.add(boolTag);
                }

                // check if valid compound
                if (restChars[0] == '{') {
                    if (restChars[restChars.length - 1] == '}') {
                        tagTypes.add(TagDef.Compound);
                        CompoundTag compoundTag = new CompoundTag(SNBTParser.read(rest));
                        tags.add(compoundTag);
                    } else {
                        throw new NbtFormatException("EOF on Compound expected and not found.");
                    }
                }

                String restNoWhite = rest.replaceAll("\\s", "");
                // check if valid list or array
                if (restChars[0] == '[') {
                    if (restChars[restChars.length - 1] == ']') {
                        // prepare to organize and read
                        String valueOnly = restNoWhite.substring(3, restNoWhite.length() - 1);
                        String[] partsInner = valueOnly.split(",");
                        StringReader partInnerReader;

                        // check definition of array type or if such definition exists.
                        String defi = restNoWhite.substring(1, 3);
                        switch (defi) {
                            case "B;": // handling a supposed byte array
                                ArrayList<Byte> byteArray = new ArrayList<>();
                                for (String partInner : partsInner) {
                                    partInnerReader = new StringReader(partInner);
                                    if (partInnerReader.isByte()) {
                                        byteArray.add((byte) partInnerReader.readInt());
                                    } else {
                                        throw new NbtFormatException("Non-Bytes in Byte Array.");
                                    }
                                }

                                tagTypes.add(TagDef.ByteArray);
                                ByteArrayTag byteArrayTag = new ByteArrayTag(byteArray);
                                tags.add(byteArrayTag);
                                break;
                            case "I;": // handling a supposed int array
                                ArrayList<Integer> intArray = new ArrayList<>();
                                for (String partInner : parts) {
                                    partInnerReader = new StringReader(partInner);
                                    if (partInnerReader.isInt()) {
                                        intArray.add(partInnerReader.readInt());
                                    } else {
                                        throw new NbtFormatException("Non-Integers in Int Array");
                                    }
                                }

                                tagTypes.add(TagDef.IntArray);
                                IntArrayTag intArrayTag = new IntArrayTag(intArray);
                                tags.add(intArrayTag);
                                break;
                            case "L;": // handling a supposed long array
                                ArrayList<Long> longArray = new ArrayList<>();
                                for (String partInner : parts) {
                                    partInnerReader = new StringReader(part);
                                    if (partInnerReader.isLong()) {
                                        longArray.add(partInnerReader.readLong());
                                    } else {
                                        throw new NbtFormatException("Non-Longs in Long Array");
                                    }
                                }

                                tagTypes.add(TagDef.LongArray);
                                LongArrayTag longArrayTag = new LongArrayTag(longArray);
                                tags.add(longArrayTag);
                                break;
                            default: // does not match applicable array types, must assume it is a list.
                                tagTypes.add(TagDef.List);
                                tags.add(parseList(rest));
                                _listLevel++; // ensure to keep track of how many nested lists we have.
                        }

                    } else {
                        throw new NbtFormatException("EOF on List/Array expected and not found.");
                    }
                }
            }
        }

        /* We now have all our items for this list in an ArrayList.
         * Because *technically* lists can only have one type,
         * if this list we read has more than one type, we must convert it to a compound.
         * So first we check first if the tagTypes variable has more than one unique value.
         * If it does NOT, we're good to keep parsing as a list.
         * If it DOES, then we convert it to a compound where the tag names are empty strings.
         */

        // check for duplicate types
        if (!hasMultiple(tagTypes)) {
            return new ListTag(tags);
        } else {
            // Ruh-roh Raggy, we discovered it has multiple tags
            CompoundTag compoundTag = new CompoundTag();

            for (NbtTag tag : tags) {
                tag.Name = ""; // needed?
                compoundTag.add("", tag);
            }

            return compoundTag;
        }
    }

    private boolean hasMultiple(ArrayList<TagDef> defs) {
        ArrayList<Byte> types = new ArrayList<>();
        for (TagDef def : defs) {
            // if it does not already exist in the array, we can add it.
            if (!types.contains(def.getByte())) {
                types.add(def.getByte());
            }
        }

        return types.size() > 1;
    }
}
