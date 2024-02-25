package com.mpcs.scratchpad.core.resources.parsing.type;

import com.mpcs.scratchpad.core.registries.Registries;

import java.util.Set;

public class TypeParsers {

    public static final NodeTypeParser NODE_TYPE_PARSER = new NodeTypeParser();
    public static final Vector3fParser VECTOR3F_PARSER = new Vector3fParser();

    private TypeParsers(){}

    public static <T> T parse(String stringValue, Class<T> parameterType) throws TypeParseException {
        Set<Object> parsers = Registries.getRegistryInstances(com.mpcs.scratchpad.core.resources.parsing.annotations.TypeParsers.class);
        for (Object object : parsers) {
            if (object instanceof TypeParser<> parser) {
                    try {
                        return (T) parser.parse(stringValue);
                    } catch (TypeParseException e) {
                        continue;
                    }
            }
        }
        throw new TypeParseException("No parser found for type: " + parameterType.getTypeName());
    }
}
