package com.mpcs.scratchpad.core.resources.parsing.type;

public interface TypeParser<T> {

    T parse(String string) throws TypeParseException;
}
