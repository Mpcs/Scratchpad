package com.mpcs.scratchpad.core.resources.parsing;

import com.mpcs.scratchpad.core.resources.parsing.type.TypeParseException;

import java.util.*;
import java.util.regex.Pattern;

public class LineParser {

    String prefix = "";
    private final List<LineParameter> parameters = new ArrayList<>();
    private String linePrefix = "";

    public LineParameter parameter(String parameterName) {
        LineParameter parameter = new LineParameter(this, parameterName);
        parameters.add(parameter);
        return parameter;
    }

    public LineParser startingWith(String linePrefix) {
        this.linePrefix = linePrefix;
        return this;
    }

    public boolean matches(String line) {
        return line.startsWith(linePrefix);
    }

    public Map<String, Object> parse(String line) throws TypeParseException {
        Map<String, Object> result = new HashMap<>();
        if (!matches(line)) {
            return result;
        }

        line = removePrefix(line);

        for (LineParameter fileSpecParameter : parameters) {
            Map.Entry<String, Object> parse = fileSpecParameter.parse(line);
            result.put(parse.getKey(), parse.getValue());

            if (parse.getValue() == null || parse.getValue() instanceof String s && s.isEmpty()) {
                continue;
            }

            line = fileSpecParameter.trim(line);
        }
        return result;
    }



    private String removePrefix(String line) {
        String[] parts = line.split(Pattern.quote(linePrefix), 2);
        if (parts.length == 1) {
            if (line.equals(linePrefix))
                return "";
            return parts[0];
        }
        return parts[1];
    }

    public KeyValueLineParameter keyValueParameter(String separator) {
        KeyValueLineParameter parameter = new KeyValueLineParameter(this, separator);
        parameters.add(parameter);
        return parameter;
    }

}
