package com.abderrahimlach.internal.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author DirectPlan
 */
public abstract class ConfigHandler {

    private final Set<ConfigurationAdapter> configurations = new HashSet<>();

    public void addConfigurations(String... configFiles) {
        ConfigurationAdapter[] configs = loadConfigurations(configFiles);
        configurations.addAll(Arrays.asList(configs));
    }

    protected abstract ConfigurationAdapter[] loadConfigurations(String... configFiles);

    public void saveConfigurations() {
        configurations.forEach(ConfigurationAdapter::saveKeys);
    }

    public void reloadConfigurations() {
        configurations.forEach(ConfigurationAdapter::loadKeys);
    }
}
