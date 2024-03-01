package com.mpcs.scratchpad.core;

import com.mpcs.scratchpad.core.input.InputManager;
import com.mpcs.scratchpad.core.rendering.Renderer;
import com.mpcs.scratchpad.core.simulation.Simulation;
import com.mpcs.scratchpad.core.resources.ResourceManager;

import java.rmi.AccessException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Context {
    public static final ThreadLocal<UUID> threadEngineUuid = new ThreadLocal<>();

    private static final Map<String, Context> instances = new HashMap<>(); // Engine UUID -> Instance

    private final Engine engine;
    private final Map<Class<?>, Object> contextElements;


    private Context(Engine engine) {
        this.engine = engine;
        this.contextElements = new HashMap<>();
    }

    void put(Class<?> clazz,Object o) {
        if (contextElements.containsKey(clazz)) {
            throw new UnsupportedOperationException("Trying to reassign " + clazz + " in context.");
        }
        contextElements.put(clazz, o);
    }

    public <T> T getInstanceOf(Class<T> clazz) {
        if (!contextElements.containsKey(clazz)) {
            throw new RuntimeException("Nothing registered for type " + clazz.getName());
        }
        return (T) contextElements.get(clazz);
    }


    static Context createContext(Engine engine) {
        String engineUuid = engine.getUuid().toString();
        if (instances.containsKey(engineUuid)) {
            throw new UnsupportedOperationException("Context already exists for this engine instance");
        }

        Context newInstance = new Context(engine);
        newInstance.put(Engine.class, engine);
        instances.put(engineUuid, newInstance);
        return newInstance;
    }

    public static Context get() {
        String uuid = threadEngineUuid.get().toString();
        if (!instances.containsKey(uuid)) {
            throw new RuntimeException("No context for engine UUID: " + uuid + ". Accessed from thread " + Thread.currentThread().getName());
        }
        return instances.get(uuid);
    }

    public static <T> T get(Class<T> clazz) {
        Context contextInstance = get();
        return contextInstance.getInstanceOf(clazz);
    }

    public Engine getEngine() {
        return engine;
    }
}
