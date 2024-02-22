package com.mpcs.scratchpad.core.resources.parsing.type;

import com.mpcs.scratchpad.core.registries.Registries;
import com.mpcs.scratchpad.core.scene.nodes.Node;
import com.mpcs.scratchpad.core.scene.nodes.annotation.RegisterNode;

import java.util.List;
import java.util.Set;

public class NodeTypeParser implements TypeParser<Class<? extends Node>>{
    @Override
    public Class<? extends Node> parse(String typeName) throws TypeParseException {
        Set<Class<?>> nodeClasses = Registries.getRegistryValues(RegisterNode.class);

        List<Class<?>> list = nodeClasses.stream().filter(val -> val.getName().endsWith(typeName)).toList();
        if (list.isEmpty()) {
            throw new TypeParseException("Type not found: " + typeName);
        }

        return (Class<? extends Node>) list.get(0);
    }
}
