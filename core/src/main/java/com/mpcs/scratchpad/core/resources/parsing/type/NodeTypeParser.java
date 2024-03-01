package com.mpcs.scratchpad.core.resources.parsing.type;

import com.mpcs.scratchpad.core.registries.Registries;
import com.mpcs.scratchpad.core.registries.annotation.Registry;
import com.mpcs.scratchpad.core.scene.nodes.Node;


import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Registry(TypeParser.class)
public class NodeTypeParser implements TypeParser<Class<? extends Node>>{
    @Override
    public Class<? extends Node> parse(String typeName) throws TypeParseException {
        Set<Class<? extends Node>> nodeClasses = Registries.getRegistryTypes(Node.class);

        List<Class<? extends Node>> list = nodeClasses.stream().filter(val -> Arrays.stream(val.getName().split("\\.")).toList().getLast().equals(typeName)).toList();
        if (list.isEmpty()) {
            throw new TypeParseException("Type not found: " + typeName);
        }

        return list.getFirst();
    }

    @Override
    public boolean matchesType(Class<?> clazz) {
        return clazz.isAssignableFrom(Node.class);
    }

}
