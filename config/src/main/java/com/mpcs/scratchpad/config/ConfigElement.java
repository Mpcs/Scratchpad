package com.mpcs.scratchpad.config;

import java.lang.reflect.Field;

public class ConfigElement {

    private Field fieldReference;
    private boolean valueSet = false;
    public ConfigElement(String classpath, String fieldName) {
        Class c;
        try {
            c = Class.forName(classpath);
            fieldReference = c.getField(fieldName);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return fieldReference.getName();
    }

    public Object getValue() {
        try {
            return fieldReference.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void setValue(String value) {
        try {
            switch (fieldReference.get(null)) {
                case Integer i:
                    fieldReference.set(null, Integer.valueOf(value));
                    break;
                case Double d:
                    fieldReference.set(null, Double.valueOf(value));
                    break;
                case Float f:
                    fieldReference.set(null, Float.valueOf(value));
                    break;
                case Boolean bool:
                    fieldReference.set(null, Boolean.valueOf(value));
                break;
                case String s:
                    fieldReference.set(null, value);
                    break;
                default:
                    Logger.error ("Unknown field type"); // TODO: Throw exception
            }
            valueSet = true;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean wasValueSet() {
        return valueSet;
    }
}
