package com.abderrahimlach;

import com.abderrahimlach.api.ChatTagAPI;
import com.abderrahimlach.commands.TagCommand;
import com.abderrahimlach.commands.TagsCommand;
import com.abderrahimlach.commands.misc.CommandRegisterer;
import com.abderrahimlach.internal.inventory.MenuListener;
import com.abderrahimlach.internal.inventory.manager.MenuManager;
import com.abderrahimlach.internal.storage.Storage;
import com.abderrahimlach.player.PlayerListener;
import com.abderrahimlach.player.PlayerManager;
import com.abderrahimlach.tag.TagManager;
import com.abderrahimlach.utility.Metrics;
import lombok.Getter;

/**
 * @author AbderrahimLach
 */
@Getter
public class TagPlugin extends BasePlugin {

    private final PlayerManager playerManager = new PlayerManager(this);
    private TagManager tagManager;
    private final MenuManager menuManager = new MenuManager();
    private final PluginConfigHandler configHandler = new PluginConfigHandler(this);
    private CommandRegisterer commandRegisterer;

    private Storage storage;

    @Override
    public void onEnable() {

        configHandler.addConfigurations("config.yml", "messages.yml");

        storage = new Storage(this);
        storage.connect();

        new ChatTagAPI(this);
        new Metrics(this, 6060);

        tagManager = new TagManager(this);
        commandRegisterer = new CommandRegisterer(this);

        if(this.isPluginEnabled("PlaceholderAPI")) {
            PAPIExpansion expansion = new PAPIExpansion(this);
            expansion.register();
        }

        registerListener(new PlayerListener(playerManager), new MenuListener(this));
        commandRegisterer.registerCommands(new TagCommand(), new TagsCommand());
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
