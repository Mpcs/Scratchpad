package com.mpcs.scratchpad.core.resources.parsing.type;

import com.mpcs.scratchpad.core.Context;
import com.mpcs.scratchpad.core.resources.ResourceManager;
import com.mpcs.scratchpad.core.scene.nodes.Node;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScriptParserLoader implements TypeParser<Class<?>> {
    @Override
    public Class<?> parse(String string) throws TypeParseException {
        if (string.isEmpty()) return null;
        Class<?> clazz = Context.get().getResourceManager().loadClass(string);
        if (clazz == null) {
            throw new TypeParseException("Error parsing class. //TODO PLS");
        }

        return clazz;
    }

    @Override
    public boolean matchesType(Class<?> clazz) {
        return clazz.isAssignableFrom(Class.class);
    }
}
