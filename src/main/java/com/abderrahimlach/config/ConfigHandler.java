package com.abderrahimlach.config;

import com.abderrahimlach.TagPlugin;
import lombok.Data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author DirectPlan
 */
@Data
public class ConfigHandler {

    private final TagPlugin plugin;

    private final Set<ConfigurationAdapter> configurations = new HashSet<>();

    public void addConfiguration(String configFile) {
        configurations.add(new ConfigurationAdapter(plugin, configFile));
    }

    public void addCollection(boolean update, String... configFiles) {
        Arrays.asList(configFiles).forEach(this::addConfiguration);
        if(update) updateConfigurationsKeys();
    }

    public void updateConfigurationsKeys() {
        configurations.forEach(this::loadKeys);
    }

    private void loadKeys(ConfigurationAdapter configurationAdapter) {
        configurationAdapter.loadConfiguration();

        for(ConfigKeys configKey : ConfigKeys.values()) {
            if(!configKey.getConfigFile().equals(configurationAdapter.getFile().getName())) continue;
            String key = configKey.getKey();
            if(!configurationAdapter.contains(key)) {
                if(configKey.isOverwrite()) {
                    configurationAdapter.set(key, configKey.getDefaultValue());
                }
                continue;
            }
            Object value = configurationAdapter.get(key);
            configKey.setDefaultValue(value);
        }
    }

    public void saveConfigurations() {
        configurations.forEach(ConfigurationAdapter::saveConfiguration);
    }
}
