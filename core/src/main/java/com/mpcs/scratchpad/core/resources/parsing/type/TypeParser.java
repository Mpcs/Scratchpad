package com.mpcs.scratchpad.core.resources.parsing.type;

public interface TypeParser<T> {

    T parse(String string) throws TypeParseException;

    default boolean matchesType(Object object) {
        try {
            abstractMethod((T)object);
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    default void abstractMethod(T element) {};
}
