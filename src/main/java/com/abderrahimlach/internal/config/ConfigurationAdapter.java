package com.abderrahimlach.internal.config;

import lombok.Getter;

import java.io.File;

/**
 * @author DirectPlan
 */
public abstract class ConfigurationAdapter implements ConfigurationMemory<String> {

    @Getter private final File file;
    public ConfigurationAdapter(File file) {
        this.file = file;
    }

    public abstract void loadConfiguration();

    public abstract void saveConfiguration();

    public void saveKeys() {

        for(ConfigKeys configKey : ConfigKeys.values()) {
            if(!configKey.getConfigFile().equals(file.getName())) continue;

            String key = configKey.getKey();
            Object value = configKey.getValue();
            set(key, value);
        }
        saveConfiguration();
    }

    public void loadKeys() {
        loadConfiguration();

        for(ConfigKeys configKey : ConfigKeys.values()) {
            if(!configKey.getConfigFile().equals(file.getName())) continue;
            String key = configKey.getKey();
            if(!configKey.isOverwrite()) {
                if(containsKey(key)) {
                    Object value = get(key);
                    configKey.setValue(value);
                }
                continue;
            }
            // if overridable
            if(!containsKey(key)) {
                set(key, configKey.getDefaultValue());
                continue;
            }
            Object value = get(key);
            configKey.setValue(value);
        }
        // For any new key that has been added to the config file.
        // This save method will ensure is added to config file
        saveConfiguration();
    }
}
