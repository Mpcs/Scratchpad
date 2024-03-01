package com.mpcs.scratchpad.core.resources.parsing;

import java.util.AbstractMap;
import java.util.Map;
import java.util.regex.Pattern;

public class KeyValueLineParameter extends LineParameter {

    private final String separator;

    public KeyValueLineParameter(LineParser parentLine, String separator) {
        super(parentLine, null);
        this.separator = separator;
    }

    @Override
    public Map.Entry<String, Object> parse(String line) {
        line = line.replaceFirst(prefix, "");
        String[] parts = line.split(Pattern.quote(postfix));
        String kvpair = parts[0];
        String[] keyvalue = kvpair.split(Pattern.quote(separator));
        return new AbstractMap.SimpleEntry<>(keyvalue[0], keyvalue[1]);
    }

}
