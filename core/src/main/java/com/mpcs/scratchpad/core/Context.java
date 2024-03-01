package com.mpcs.scratchpad.core;

import com.mpcs.scratchpad.core.service.EngineService;

import java.util.*;

public class Context {
    public static final ThreadLocal<UUID> threadEngineUuid = new ThreadLocal<>();

    private static final Map<String, Context> instances = new HashMap<>(); // Engine UUID -> Instance

    private final Engine engine;
    private final Map<Class<? extends EngineService>, EngineService> contextElements;


    private Context(Engine engine) {
        this.engine = engine;
        this.contextElements = new HashMap<>();
    }

    void put(Class<? extends EngineService> clazz, EngineService o) {
        if (contextElements.containsKey(clazz)) {
            throw new UnsupportedOperationException("Trying to reassign " + clazz + " in context.");
        }
        contextElements.put(clazz, o);
    }

    public <T extends EngineService> T getInstanceOf(Class<T> clazz) {
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

    public static <T extends EngineService> T get(Class<T> clazz) {
        Context contextInstance = get();
        return contextInstance.getInstanceOf(clazz);
    }

    public Engine getEngineInstance() {
        return engine;
    }

    public static Engine getEngine() {
        Context contextInstance = get();
        return contextInstance.getEngineInstance();
    }

    public List<EngineService> getAllSortedByPriority() {
        return contextElements.values().stream().sorted(Comparator.comparing(EngineService::getPriority)).toList();
    }
}
