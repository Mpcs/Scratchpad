package com.mpcs.config;

import com.mpcs.config.annotations.Config;
import com.mpcs.logging.Logger;

import java.io.File;
import java.io.IOException;

public final class ConfigManager {
    public static void init() {
        Logger.log("Initializing config");
        try {
            Class variablesClass = Class.forName("com.mpcs.config.ConfigVars");
            Logger.log(variablesClass.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        File configFile = new File("config.conf");
        if (configFile.createNewFile()) {
            System.out.println("Exists");
        } else {
            System.out.println("creating");
        }
    }

}
