package com.abderrahimlach.commands.misc;

import co.aikar.commands.*;
import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.internal.config.ConfigHandler;
import com.abderrahimlach.internal.config.ConfigKeys;
import com.abderrahimlach.player.PlayerManager;
import com.abderrahimlach.tag.TagManager;
import com.abderrahimlach.tag.Tag;
import org.bukkit.ChatColor;

import java.util.Arrays;

/**
 * @author DirectPlan
 */

public class CommandRegisterer {

    private final BukkitCommandManager commandManager;
    private final TagPlugin plugin;

    public CommandRegisterer(TagPlugin plugin) {
        this.plugin = plugin;
        commandManager = new BukkitCommandManager(plugin);
        commandManager.enableUnstableAPI("help");
        commandManager.setFormat(MessageType.HELP, ChatColor.AQUA, ChatColor.BLUE);
        commandManager.setFormat(MessageType.SYNTAX, ChatColor.AQUA, ChatColor.BLUE);
        commandManager.setFormat(MessageType.INFO, ChatColor.AQUA, ChatColor.BLUE);

        registerCommandContexts();
    }

    public void registerCommands(BaseCommand... commands){
        Arrays.asList(commands).forEach(commandManager::registerCommand);
    }

    private void registerCommandContexts() {
        TagManager tagManager = plugin.getTagManager();
        commandManager.registerDependency(PlayerManager.class, plugin.getPlayerManager());
        commandManager.registerDependency(TagManager.class, tagManager);
        commandManager.registerDependency(ConfigHandler.class, plugin.getConfigHandler());
        CommandContexts<BukkitCommandExecutionContext> commandContexts = commandManager.getCommandContexts();
        commandContexts.registerContext(Tag.class, resolver -> {
            String arg = resolver.popFirstArg();
            Tag tag = tagManager.getTag(arg);
            if(tag == null) {
                throw new InvalidCommandArgument(ConfigKeys.TAG_NOT_FOUND.getValue());
            }
            return tag;
        });
    }
}
