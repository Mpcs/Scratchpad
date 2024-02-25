package com.mpcs.scratchpad.core.resources.parsing.type;

import com.mpcs.scratchpad.core.registries.Registries;

import java.util.Set;

public class TypeParsers {

    public static final NodeTypeParser NODE_TYPE_PARSER = new NodeTypeParser();
    public static final Vector3fParser VECTOR3F_PARSER = new Vector3fParser();

    private TypeParsers(){}

    public static <T> T parse(String stringValue, Class<T> parameterType) throws TypeParseException {
        Set<TypeParser> parsers = Registries.getRegistryInstances(TypeParser.class);

        for (TypeParser<?> typeParser : parsers) {
            if (typeParser.matchesType(parameterType)) {
                return (T)typeParser.parse(stringValue);
            }
        }
        throw new TypeParseException("No parser found for type: " + parameterType.getTypeName());
    }
}
