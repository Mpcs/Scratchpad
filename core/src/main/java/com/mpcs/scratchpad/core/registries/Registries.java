package com.mpcs.scratchpad.core.registries;

import com.mpcs.scratchpad.core.registries.annotation.RegistryAnnotationProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Registries {

    private static Map<Class<?>, Set<Class<?>>> loadedTypeRegistries = new HashMap<>();
    private static Map<Class<?>, Set<Object>> loadedInstanceRegistries = new HashMap<>();

    public static Set<Class<?>> getRegistryTypes(Class<?> registryType) {
        return getOrLoadTypeRegistry(registryType);
    }

    private static Set<Class<?>> getOrLoadTypeRegistry(Class<?> registryType) {
        if (loadedTypeRegistries.containsKey(registryType)) {
            return loadedTypeRegistries.get(registryType);
        }

        loadedTypeRegistries.put(registryType, loadTypeRegistry(registryType));
        return loadedTypeRegistries.get(registryType);
    }

    private static Set<Class<?>> loadTypeRegistry(Class<?> registryType) {
        ClassLoader classLoader = Registries.class.getClassLoader();
        URL resource = classLoader.getResource(registryType.getSimpleName() + RegistryAnnotationProcessor.FILE_EXTENSION);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream()))) {
            Set<Class<?>> loadedClasses = new HashSet<>();

            String line = reader.readLine();
            while (line != null) {
                loadedClasses.add(Class.forName(line));
                line = reader.readLine();
            }
            return loadedClasses;
        }catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Set<Object> getRegistryInstances(Class<?> registryType) {
        return getOrLoadInstanceRegistry(registryType);
    }

    private static Set<Object> getOrLoadInstanceRegistry(Class<?> registryType) {
        if (loadedInstanceRegistries.containsKey(registryType)) {
            return loadedInstanceRegistries.get(registryType);
        }

        loadedInstanceRegistries.put(registryType, loadInstanceRegistry(registryType));
        return loadedInstanceRegistries.get(registryType);
    }

    private static Set<Object> loadInstanceRegistry(Class<?> registryType) {
        Set<Class<?>> types = getOrLoadTypeRegistry(registryType);
        Set<Object> instances = new HashSet<>();
        for (Class<?> type : types) {
            try {
                Object instance = type.getDeclaredConstructor().newInstance();
                instances.add(instance);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return instances;
    }
}
