package com.mpcs.scratchpad.core.registries;

import com.mpcs.scratchpad.core.registries.annotation.RegistryAnnotation;
import com.mpcs.scratchpad.core.registries.annotation.RegistryAnnotationProcessor;
import com.mpcs.scratchpad.core.scene.nodes.annotation.RegisterNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Registries {

    private static Map<Class<?>, Set<Class<?>>> loadedRegistries = new HashMap<>();

    public static Set<Class<?>> getRegistryValues(Class<?> registryType) {
        return getOrLoadRegistry(registryType);
    }

    private static Set<Class<?>> getOrLoadRegistry(Class<?> registryType) {
        if (loadedRegistries.containsKey(registryType)) {
            return loadedRegistries.get(registryType);
        }

        loadedRegistries.put(registryType, loadRegistry(registryType));
        return loadedRegistries.get(registryType);
    }

    private static Set<Class<?>> loadRegistry(Class<?> registryType) {
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

}
