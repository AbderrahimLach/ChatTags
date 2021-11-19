package com.abderrahimlach;

import com.abderrahimlach.api.ChatTagAPI;
import com.abderrahimlach.commands.TagCommand;
import com.abderrahimlach.commands.TagsCommand;
import com.abderrahimlach.commands.misc.CommandRegisterer;
import com.abderrahimlach.config.ConfigHandler;
import com.abderrahimlach.data.connection.Storage;
import com.abderrahimlach.listeners.MenuListener;
import com.abderrahimlach.listeners.PlayerListener;
import com.abderrahimlach.management.MenuManager;
import com.abderrahimlach.management.PlayerManager;
import com.abderrahimlach.management.TagManager;
import com.abderrahimlach.utility.Metrics;
import lombok.Getter;

/**
 * @author AbderrahimLach
 */
@Getter
public class TagPlugin extends BasePlugin {

    private final PlayerManager playerManager = new PlayerManager(this);
    private final TagManager tagManager = new TagManager(this);
    private final MenuManager menuManager = new MenuManager();
    private final ConfigHandler configHandler = new ConfigHandler(this);
    private final CommandRegisterer commandRegisterer = new CommandRegisterer(this);

    private Storage storage;

    @Override
    public void onEnable() {

        configHandler.addCollection(true, "config.yml", "messages.yml");

        storage = new Storage(this);
        log("Connecting to the " + this.storage.getName() + " Storage...");
        storage.connect();
        log("Connection successfully established!");

        new ChatTagAPI(this);
        new Metrics(this, 6060);
        if(this.isPluginEnabled("PlaceholderAPI")) {
            PAPIExpansion expansion = new PAPIExpansion(this);
            expansion.register();
        }

        registerListener(new PlayerListener(playerManager), new MenuListener(menuManager));
        commandRegisterer.registerCommands(new TagCommand(this), new TagsCommand(this));
    }

    @Override
    public void onDisable() {
        configHandler.saveConfigurations();

        playerManager.savePlayers();
        tagManager.saveTags();

        log("Closing storage connection...");
        storage.close();
    }
}
