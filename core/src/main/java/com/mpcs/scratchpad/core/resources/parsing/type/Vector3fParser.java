package com.mpcs.scratchpad.core.resources.parsing.type;

import com.mpcs.scratchpad.core.registries.annotation.Registry;
import com.mpcs.scratchpad.core.rendering.mesh.Mesh3D;
import org.joml.Vector3f;

@Registry(TypeParser.class)
public class Vector3fParser implements TypeParser<Vector3f>{
    @Override
    public Vector3f parse(String string) throws TypeParseException {
        string = string.replace("(", "").replace(")", "");
        String[] elements = string.split(",");

        if (elements.length != 3) {
            throw new TypeParseException(Vector3f.class, string);
        }

        try {
            float x = Float.parseFloat(elements[0]);
            float y = Float.parseFloat(elements[1]);
            float z = Float.parseFloat(elements[2]);
            return new Vector3f(x, y, z);
        } catch (NumberFormatException e) {
            throw new TypeParseException(Vector3f.class, string);
        }


    }

    @Override
    public boolean matchesType(Class<?> clazz) {
        return clazz.isAssignableFrom(Vector3f.class);
    }

}
