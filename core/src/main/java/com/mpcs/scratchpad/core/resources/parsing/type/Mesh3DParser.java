package com.mpcs.scratchpad.core.resources.parsing.type;

import com.mpcs.scratchpad.core.Context;
import com.mpcs.scratchpad.core.rendering.mesh.Mesh3D;
import com.mpcs.scratchpad.core.resources.parsing.annotations.TypeParsers;

import java.io.IOException;

@TypeParsers
public class Mesh3DParser implements TypeParser<Mesh3D> {
    @Override
    public Mesh3D parse(String string) throws TypeParseException {
        try {
            return Context.get().getResourceManager().getResourceMesh(string);
        } catch (IOException e) {
            throw new TypeParseException("Mesh file not found: " + string);
        }
    }
}
