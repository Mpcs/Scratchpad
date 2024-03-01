package com.mpcs.scratchpad.core.resources.parsing.type;

import com.mpcs.scratchpad.core.Context;
import com.mpcs.scratchpad.core.registries.annotation.Registry;
import com.mpcs.scratchpad.core.rendering.mesh.Mesh3D;
import com.mpcs.scratchpad.core.resources.ResourceManager;

import java.io.IOException;

@Registry(TypeParser.class)
public class Mesh3DParser implements TypeParser<Mesh3D> {
    @Override
    public Mesh3D parse(String string) throws TypeParseException {
        try {
            return Context.get(ResourceManager.class).getResourceMesh(string);
        } catch (IOException e) {
            throw new TypeParseException("Mesh file not found: " + string);
        }
    }

    @Override
    public boolean matchesType(Class<?> clazz) {
        return clazz.isAssignableFrom(Mesh3D.class);
    }

}
