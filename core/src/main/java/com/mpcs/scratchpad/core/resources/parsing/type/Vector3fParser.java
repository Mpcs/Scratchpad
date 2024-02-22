package com.mpcs.scratchpad.core.resources.parsing.type;

import org.joml.Vector3f;

public class Vector3fParser implements TypeParser<Vector3f>{
    @Override
    public Vector3f parse(String string) throws TypeParseException {
        string = string.replace("(", "").replace(")", "");
        String[] elements = string.split(",");

        if (elements.length != 3) {
            throw new TypeParseException(Vector3f.class, string);
        }

        float x = 0;
        float y = 0;
        float z = 0;
        try {
            x = Float.parseFloat(elements[0]);
            y = Float.parseFloat(elements[1]);
            z = Float.parseFloat(elements[2]);
        } catch (NumberFormatException e) {
            throw new TypeParseException(Vector3f.class, string);
        }

        return new Vector3f(x, y, z);
    }
}
