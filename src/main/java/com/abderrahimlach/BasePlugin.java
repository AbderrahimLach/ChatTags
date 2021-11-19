package com.abderrahimlach;

import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * @author AbderrahimLach
 */
@Getter
public abstract class BasePlugin extends JavaPlugin {

    public void registerListener(Listener... listeners){
        PluginManager pluginManager = getServer().getPluginManager();
        for(Listener listener : listeners){
            pluginManager.registerEvents(listener, this);
        }
    }

    public boolean isPluginEnabled(String plugin) {
        PluginManager pluginManager = getServer().getPluginManager();
        return pluginManager.isPluginEnabled(plugin);
    }

    public void log(String message){
        getLogger().log(Level.WARNING, message);
    }
}
