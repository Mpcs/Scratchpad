package com.mpcs.scratchpad.core;

public class EngineCreationException extends Exception {

    public EngineCreationException(String message) {
        super(message);
    }

    public EngineCreationException(Exception e) {
        super(e);
    }
}
