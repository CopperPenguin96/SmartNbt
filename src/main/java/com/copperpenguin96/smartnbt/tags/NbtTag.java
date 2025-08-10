package com.copperpenguin96.smartnbt.tags;

import com.copperpenguin96.smartnbt.NbtFormatException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class NbtTag {

    protected static final Charset ENCODING = StandardCharsets.UTF_8;

    private TagDef _def;

    protected void setID(TagDef def) {
        _def = def;
    }

    public TagDef getID() {
        return _def;
    }

    private byte _pay;

    public byte getPayload() {
        return _pay;
    }

    protected void setPayload(byte pay) {
        _pay = pay;
    }

    protected NbtTag[] toArray(List<NbtTag> tags) {
        NbtTag[] arr = new NbtTag[tags.size()];

        for (int x = 0; x < tags.size(); x++) {
            arr[x] = tags.get(x);
        }

        return arr;
    }

    public String Name;

    private Object Value;

    public Object getValue() {
        return Value;
    }

    protected void setValue(Object value) {
        Value = value;
    }

    protected NbtTag(String name) {
        Name = name;
    }

    protected NbtTag(Object value) {
        Value = value;
    }

    protected NbtTag(String name, Object value) {
        this(name);
        Value = value;
    }

    public boolean getBoolValue() {
        return getByteValue() == 1;
    }

    public byte getByteValue() {
        return (byte)Value;
    }

    public byte[] getByteArrayValue() {
        return (byte[])Value;
    }

    public NbtTag[] getItems() {
        return (NbtTag[])Value;
    }

    public double getDoubleValue() {
        return (double)Value;
    }

    public float getFloatValue() {
        return (float)Value;
    }

    public int getIntValue() {
        return (int)Value;
    }

    public int[] getIntArrayValue() {
        return (int[])Value;
    }

    public long getLongValue() {
        return (long)Value;
    }

    public long[] getLongArrayValue() {
        return (long[])Value;
    }

    public short getShortValue() {
        return (short)Value;
    }

    public String getStringValue() {
        return (String)Value;
    }

    protected void writeToStream(OutputStream stream) {
        try {
            stream.write(getID().getByte());

            if (Name != null) {
                stream.write((byte)Name.length());
                stream.write(Name.getBytes(ENCODING));
            }

            stream.write(getPayload());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeTag(OutputStream stream, NbtTag tag) throws NbtFormatException {
        switch (tag.getID()) {
            case End:
                EndTag endTag = (EndTag)tag;
                endTag.writeToStream(stream);
                break;
            case Byte:
                ByteTag byteTag = (ByteTag)tag;
                byteTag.writeToStream(stream);
                break;
            case Short:
                ShortTag shortTag = (ShortTag)tag;
                shortTag.writeToStream(stream);
                break;
            case Int:
                IntTag intTag = (IntTag)tag;
                intTag.writeToStream(stream);
                break;
            case Long:
                LongTag longTag = (LongTag)tag;
                longTag.writeToStream(stream);
                break;
            case Float:
                FloatTag floatTag = (FloatTag)tag;
                floatTag.writeToStream(stream);
                break;
            case Double:
                DoubleTag doubleTag = (DoubleTag)tag;
                doubleTag.writeToStream(stream);
                break;
            case ByteArray:
                ByteArrayTag byteArrayTag = (ByteArrayTag)tag;
                byteArrayTag.writeToStream(stream);
                break;
            case String:
                StringTag stringTag = (StringTag)tag;
                stringTag.writeToStream(stream);
                break;
            case List:
                ListTag listTag = (ListTag)tag;
                listTag.writeToStream(stream);
                break;
            case Compound:
                CompoundTag compoundTag = (CompoundTag)tag;
                compoundTag.writeToStream(stream);
                break;
            case IntArray:
                IntArrayTag intArrayTag = (IntArrayTag)tag;
                intArrayTag.writeToStream(stream);
                break;
            case LongArray:
                LongArrayTag longArrayTag = (LongArrayTag)tag;
                longArrayTag.writeToStream(stream);
                break;
            default:
                throw new NbtFormatException("Unrecognized NBT Tag.");
        }
    }

    public static NbtTag readFromStream(InputStream stream, boolean seekName) throws IOException, NbtFormatException {
        TagDef def = TagDef.getDef((byte) stream.read());
        NbtTag tag = null;

        String name = null;
        ByteBuffer buffer;

        if (seekName) {
            byte nameLength = (byte)stream.read();
            byte[] bts = new byte[nameLength];
            stream.read(bts, 0, nameLength);
            name = new String(bts, StandardCharsets.UTF_8);
        }

        switch (def) {
            case End:
                return new EndTag();
            case Byte:
                byte by = (byte)stream.read();
                tag = new ByteTag(name, by);
                break;
            case Short:
                buffer = ByteBuffer.wrap(stream.readNBytes(2));
                short sh = buffer.getShort();
                tag = new ShortTag(name, sh);
                break;
            case Int:
                buffer = ByteBuffer.wrap(stream.readNBytes(4));
                int i = buffer.getInt();
                tag = new IntTag(name, i);
                break;
            case Long:
                buffer = ByteBuffer.wrap(stream.readNBytes(8));
                long l = buffer.getLong();
                tag = new LongTag(name, l);
                break;
            case Float:
                buffer = ByteBuffer.wrap(stream.readNBytes(4));
                float f = buffer.getFloat();
                tag = new FloatTag(name, f);
                break;
            case Double:
                buffer = ByteBuffer.wrap(stream.readNBytes(8));
                double d = buffer.getDouble();
                tag = new DoubleTag(name, d);
                break;
            case ByteArray:
                ByteBuffer readLength = ByteBuffer.wrap(stream.readNBytes(4));
                int length = readLength.getInt();
                byte[] bts = stream.readNBytes(length);

                tag = new ByteArrayTag(name, bts);
                break;
            case String:
                byte strLength = (byte)stream.read();
                byte[] strBts = stream.readNBytes(strLength);
                String s = new String(strBts, StandardCharsets.UTF_8);
                tag = new StringTag(name, s);
                break;
            case List:
                TagDef listType = TagDef.getDef((byte)stream.read());
                byte listLength = (byte)stream.read();

                ArrayList<NbtTag> liTag = new ArrayList<>();
                for (int x = 0; x < listLength; x++) {
                    NbtTag tv = readFromStream(stream, false);
                    if (tv.getID().getByte() != listType.getByte()) {
                        throw new NbtFormatException("List type mixmatch");
                    }

                    liTag.add(tv);
                }

                tag = new ListTag(name, liTag);
                break;
            case Compound:
                CompoundTag cTag = new CompoundTag(name);
                boolean stop = false;
                ArrayList<NbtTag> cmpItem = new ArrayList<>();
                do {
                    NbtTag t = readFromStream(stream, true);
                    if (t.getID() == TagDef.End) {
                        stop = true;
                    }

                    if (!stop) {
                        cmpItem.add(t);
                    }
                } while (!stop);

                tag = cTag;
                break;
            case IntArray:
                readLength = ByteBuffer.wrap(stream.readNBytes(4));
                length = readLength.getInt();

                IntArrayTag t = new IntArrayTag(name);
                for (int x = 0; x < length; x++) {
                    IntTag iTag = (IntTag)readFromStream(stream, false);
                    t.add(iTag.getIntValue());
                }

                tag = t;
                break;
            case LongArray:
                readLength = ByteBuffer.wrap(stream.readNBytes(4));
                length = readLength.getInt();

                LongArrayTag longTag = new LongArrayTag(name);
                for (int x = 0; x < length; x++) {
                    LongTag iTag = (LongTag)readFromStream(stream, false);
                    longTag.add(iTag.getIntValue());
                }

                tag = longTag;
                break;
        }

        return tag;
    }

    /**
     * Gets the SNBT representation of this tag without any serializer options.
     * @return An SNBT string with no options.
     */
    @Override
    public abstract String toString();

    /**
     * Creates a compound tag.
     * @param name The name of the tag.
     * @param values The tags to associate with this tag.
     * @return A compound tag with the name and values specified.
     */
    public static CompoundTag createCompound(String name, NbtTag[] values) {
        return new CompoundTag(name, values);
    }

    /**
     * Creates a list tag.
     * @param name The name of the tag.
     * @param values The values to associate with this tag.
     * @return A list tag with the name and values specified.
     */
    public static ListTag createList(String name, NbtTag[] values) {
        try {
            return new ListTag(name, values);
        } catch (NbtFormatException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a tag based on the provided value.
     * *NOTE*: This does not work with lists and compounds.
     * Use NbtTag.createCompound() and NbtTag.createList() respectively
     * @param name What to name the tag
     * @param value The value of the tag
     * @return A complete NBT Tag with a name and value.
     * @param <T> The type of the value.
     */
    public static <T> NbtTag create(String name, T value) {
        NbtTag tag = null;
        if (value instanceof boolean) {
            tag = new BoolTag(name, (boolean)value);
        } else if (value instanceof byte) {
            tag = new ByteTag(name, (byte)value);
        } else if (value instanceof short) {
            tag = new ShortTag(name, (short)value);
        } else if (value instanceof int) {
            tag = new IntTag(name, (int)value);
        } else if (value instanceof long) {
            tag = new LongTag(name, (long)value);
        } else if (value instanceof float) {
            tag = new FloatTag(name, (float)value);
        } else if (value instanceof double) {
            tag = new DoubleTag(name, (double)value);
        } else if (value instanceof String) {
            tag = new StringTag(name, (String)value);
        } else if (value instanceof byte[]) {
            tag = new ByteArrayTag(name, (byte[])value);
        } else if (value instanceof int[]) {
            tag = new IntArrayTag(name, (int[])value);
        } else if (value instanceof long[]) {
            tag = new LongArrayTag(name, (long[])value);;
        }

        return tag;
        /*else if (value.getClass().isArray()) {
           // todo  if (value instanceof )
        }*/
    }

    /**
     * Creates an end tag.
     * @return An end tag
     */
    public static EndTag createEnd() {
        return new EndTag();
    }
}
