package com.mpcs.scratchpad.core.resources.parsing;

import com.mpcs.scratchpad.core.resources.parsing.type.NodeTypeParser;
import com.mpcs.scratchpad.core.resources.parsing.type.TypeParseException;
import com.mpcs.scratchpad.core.resources.parsing.type.TypeParser;

import java.util.AbstractMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LineParameter {
    private final String name;
    String postfix = "";
    String prefix = "";
    LineParser parentLine;

    TypeParser<?> typeParser;

    public LineParameter(LineParser parentLine, String parameterName) {
        this.parentLine = parentLine;
        this.name = parameterName;
    }

    public LineParser until(String postfix) {
        this.postfix = postfix;
        return parentLine;
    }

    public LineParameter withPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public Map.Entry<String, Object> parse(String line) throws TypeParseException {
        String stringValue = getValue(line);
        Object objValue = stringValue;
        if (typeParser != null) {
            objValue = typeParser.parse(stringValue);
        }
        return new AbstractMap.SimpleEntry<>(name, objValue);
    }

    private String getValue(String line) {
        line = line.replaceFirst(prefix, "");
        String[] parts = line.split(Pattern.quote(postfix));
        if (parts.length == 0) {
            return "";
        }
        return parts[0];
    }

    public String trim(String line) {
        return line.replaceFirst(prefix, "").replaceFirst(getValue(line), "").replaceFirst(postfix, "");
    }

    public LineParameter parsedWith(TypeParser<?> typeParser) {
        this.typeParser = typeParser;
        return this;
    }
}
