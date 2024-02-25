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
import java.util.stream.Collectors;

public class Registries {

    private static Map<Class<?>, Set<Class<?>>> loadedTypeRegistries = new HashMap<>();
    private static Map<Class<?>, Set<Object>> loadedInstanceRegistries = new HashMap<>();

    public static <T> Set<Class<? extends T>> getRegistryTypes(Class<T> registryType) {
        return getOrLoadTypeRegistry(registryType);
    }

    private static <T> Set<Class<? extends T>> getOrLoadTypeRegistry(Class<T> registryType) {
        if (loadedTypeRegistries.containsKey(registryType)) {
            return loadedTypeRegistries.get(registryType).stream().map(clazz -> (Class<? extends T>)clazz).collect(Collectors.toSet());
        }

        loadedTypeRegistries.put(registryType, loadTypeRegistry(registryType));
        return loadedTypeRegistries.get(registryType).stream().map(clazz -> (Class<? extends T>)clazz).collect(Collectors.toSet());
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

    public static <T> Set<T> getRegistryInstances(Class<T> registryType) {
        return getOrLoadInstanceRegistry(registryType);
    }

    private static <T> Set<T> getOrLoadInstanceRegistry(Class<T> registryType) {
        if (loadedInstanceRegistries.containsKey(registryType)) {
            return (Set<T>) loadedInstanceRegistries.get(registryType);
        }

        loadedInstanceRegistries.put(registryType, loadInstanceRegistry(registryType).stream().map(obj -> (Object) obj).collect(Collectors.toSet()));
        return (Set<T>) loadedInstanceRegistries.get(registryType);
    }

    private static <T> Set<T> loadInstanceRegistry(Class<T> registryType) {
        Set<Class<? extends T>> types = getOrLoadTypeRegistry(registryType);
        Set<T> instances = new HashSet<>();
        for (Class<? extends T> type : types) {
            try {
                T instance = type.getDeclaredConstructor().newInstance();
                instances.add(instance);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return instances;
    }
}
