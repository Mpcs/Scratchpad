package com.mpcs.scratchpad.core.resources.obj;

public enum VertexDataFormat {
    POSITION(true, false, false),
    POSITION_TEXTURE(true, true, false),
    POSITION_TEXTURE_NORMAL(true, true, true),
    POSITION_NORMAL(true,false,true);

    public final boolean hasPosition;
    public final boolean hasTexture;
    public final boolean hasNormal;

    VertexDataFormat(boolean hasPosition, boolean hasTexture, boolean hasNormal) {
        this.hasPosition = hasPosition;
        this.hasTexture = hasTexture;
        this.hasNormal = hasNormal;
    }
}
