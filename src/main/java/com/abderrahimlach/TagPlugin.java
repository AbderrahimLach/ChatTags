package com.abderrahimlach;

import com.abderrahimlach.api.ChatTagAPI;
import com.abderrahimlach.commands.TagCommand;
import com.abderrahimlach.commands.TagsCommand;
import com.abderrahimlach.config.ConfigurationAdapter;
import com.abderrahimlach.data.connection.Storage;
import com.abderrahimlach.listeners.MenuListener;
import com.abderrahimlach.listeners.PlayerListener;
import com.abderrahimlach.management.MenuManager;
import com.abderrahimlach.management.PlayerManager;
import com.abderrahimlach.management.TagManager;
import lombok.Getter;

/**
 * @author AbderrahimLach
 */
@Getter
public class TagPlugin extends BasePlugin {

    private PlayerManager playerManager;
    private TagManager tagManager;
    private MenuManager menuManager;

    private Storage storage;

    private ConfigurationAdapter defaultConfiguration, messagesConfiguration;

    @Override
    public void onEnable() {

        this.defaultConfiguration = new ConfigurationAdapter(this, "config.yml");
        this.messagesConfiguration = new ConfigurationAdapter(this, "messages.yml");

        this.storage = new Storage(this, defaultConfiguration);
        log("Connecting to the " + this.storage.getName() + " Storage...");
        this.storage.connect();
        log("Connection successfully established!");

        this.playerManager = new PlayerManager(this);
        this.tagManager = new TagManager(this);
        this.menuManager = new MenuManager();

        new ChatTagAPI(this);
        if(this.isPluginEnabled("PlaceholderAPI")) {
            PAPIExpansion expansion = new PAPIExpansion(this);
            expansion.register();
        }

        registerListener(new PlayerListener(this), new MenuListener(this));
        registerCommands(new TagCommand(this), new TagsCommand(this));
    }

    @Override
    public void onDisable() {
        this.defaultConfiguration.saveConfiguration();
        this.messagesConfiguration.saveConfiguration();

        this.playerManager.savePlayers();
        this.tagManager.saveTags();

        log("Closing storage connection...");
        this.storage.close();
    }
}
