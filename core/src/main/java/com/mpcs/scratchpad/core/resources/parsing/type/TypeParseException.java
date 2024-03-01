package com.mpcs.scratchpad.core.resources.parsing.type;

public class TypeParseException extends Exception{

    public TypeParseException(String message) {
        super(message);
    }

    public TypeParseException(Throwable e) {
        super(e);
    }

    public TypeParseException(Class<?> classType, String string) {
        super("For string: \"" + string + "\" as: " + classType.getTypeName());
    }
}
