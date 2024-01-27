package com.mpcs.scratchpad.config;

import java.io.*;
import java.util.Map;

public final class ConfigManager {

    public static void init() {
        init("config.conf");
    }
    public static void init(String configFilePath) {
        //Logger.info("Initializing config");
        Map<String, ConfigElement> configElements = getConfigFields();

        File configFile = new File(configFilePath);
        boolean newFileCreated;
        try {
            newFileCreated = configFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (newFileCreated) {
            //Logger.info("Config file not found. Generating default");
            try {
                FileWriter writer = new FileWriter(configFile);
                writer.write(generateDefaultConfig(configElements));
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            //Logger.debug("Found config file");
            try {
                parseConfig(configFile, configElements);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void parseConfig(File configFile, Map<String, ConfigElement> configElements) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(configFile));

        String currentLine;
        while ((currentLine = fileReader.readLine()) != null) {
            String[] elements = currentLine.split(": ");
            if (configElements.containsKey(elements[0])) {
                configElements.get(elements[0]).setValue(elements[1]);
            } else {
                Logger.warn("Unknown key found in config: '" + elements[0] + "'");
            }
        }
        fileReader.close();

        FileWriter fileWriter = new FileWriter(configFile, true);
        for (ConfigElement element : configElements.values()) {
            if (!element.wasValueSet()) {
                Logger.warn("Key: '" + element.getName() + "' not found in config file. Appending default.");
                fileWriter.write(element.getName() + ": " + element.getValue() + "\n");
            }
        }
        fileWriter.close();
    }

    private static Map<String, ConfigElement> getConfigFields() {
        try {
            Class<?> configVarsClass = Class.forName("com.mpcs.config.ConfigVars");

            return (Map<String, ConfigElement>) configVarsClass.getField("elements").get(null);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateDefaultConfig(Map<String, ConfigElement> configElements) {
        StringBuilder configBuilder = new StringBuilder();
        for (ConfigElement element : configElements.values()) {
            configBuilder.append(element.getName())
                    .append(": ")
                    .append(element.getValue())
                    .append("\n");
        }
        return configBuilder.toString();
    }
}
