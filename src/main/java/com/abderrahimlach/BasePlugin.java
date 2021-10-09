package com.abderrahimlach;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.MessageType;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * @author AbderrahimLach
 */
@Getter
public abstract class BasePlugin extends JavaPlugin {

    public void registerCommands(BaseCommand... commands){
        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.enableUnstableAPI("help");
        manager.setFormat(MessageType.HELP, ChatColor.AQUA, ChatColor.BLUE);
        manager.setFormat(MessageType.SYNTAX, ChatColor.AQUA, ChatColor.BLUE);
        manager.setFormat(MessageType.INFO, ChatColor.AQUA, ChatColor.BLUE);
        for(BaseCommand command : commands){
            manager.registerCommand(command);
        }
    }

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
