package com.copperpenguin96.smartnbt.utils;

import com.copperpenguin96.smartnbt.tags.NbtTag;

/**
 * Useful for third party tools to organize their nbt structures.
 * For instance, level.dat can be created in Level.java and implementing this.
 */
public interface iTagProducer {
    NbtTag getNbt();
}
