package com.mpcs.scratchpad.core.resources.parsing;

import com.mpcs.scratchpad.core.resources.parsing.type.TypeParseException;
import com.mpcs.scratchpad.core.resources.parsing.type.TypeParser;

import java.text.ParseException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LineParameter {
    private final String name;
    String postfix = "";
    String prefix = "";
    LineParser parentLine;
    boolean optional = false;

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

    public LineParameter setOptional() {
        this.optional = true;
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
        if (line.startsWith(prefix)) {
            line = line.replaceFirst(prefix, "");
            String[] parts = line.split(Pattern.quote(postfix));
            return parts.length > 0 ? parts[0] : "";
        }
        if (optional) {
            return "";
        } else {
            throw new RuntimeException("Line Parameter not found, and is not optional. in line: " + line +  "  prefix: " + prefix);
        }

    }

    public String trim(String line) {
        return line.replaceFirst(prefix, "").replaceFirst(getValue(line), "").replaceFirst(postfix, "");
    }

    public LineParameter parsedWith(TypeParser<?> typeParser) {
        this.typeParser = typeParser;
        return this;
    }
}
