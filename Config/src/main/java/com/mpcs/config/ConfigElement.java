package com.mpcs.config;

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
}
