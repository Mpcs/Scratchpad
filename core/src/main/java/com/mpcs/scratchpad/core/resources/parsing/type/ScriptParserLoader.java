package com.mpcs.scratchpad.core.resources.parsing.type;

import com.mpcs.scratchpad.core.Context;
import com.mpcs.scratchpad.core.resources.ResourceManager;

public class ScriptParserLoader implements TypeParser<Class<?>> {
    @Override
    public Class<?> parse(String string) throws TypeParseException {
        if (string.isEmpty()) return null;
        Class<?> clazz = Context.get(ResourceManager.class).loadClass(string);
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
