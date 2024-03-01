package com.mpcs.scratchpad.core;

import com.mpcs.scratchpad.core.resources.ResourceManager;
import com.mpcs.scratchpad.core.service.EngineService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

public class Engine {
    private final Context context;
    private final UUID uuid;

    protected Engine(List<Class<? extends EngineService>> serviceClasses, String projectPath) throws EngineCreationException {
        this(serviceClasses, new ResourceManager(projectPath));
    }

    protected Engine(List<Class<? extends EngineService>> serviceClasses, ResourceManager resourceManager) throws EngineCreationException {
        uuid = UUID.randomUUID();

        context = Context.createContext(this);
        context.put(ResourceManager.class, resourceManager);

        for (Class<? extends EngineService> serviceClass : serviceClasses) {
            try {
                Constructor<? extends EngineService> constructor = serviceClass.getConstructor();
                context.put(serviceClass, constructor.newInstance());
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                     InvocationTargetException e) {
                throw new EngineCreationException(e);
            }
        }
    }

    public void start() {
        List<EngineService> services = context.getAllSortedByPriority();
        for (EngineService service : services) {
            service.init(context);
        }

        for (EngineService service : services) {
            service.start();
        }

    }

    public void stop() {
        for (EngineService service : context.getAllSortedByPriority().reversed()) {
            service.stop();
        }
    }

    public Context getContext() {
        return context;
    }

    public UUID getUuid() {
        return uuid;
    }
}
