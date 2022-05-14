package com.abderrahimlach.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.internal.config.ConfigKeys;
import com.abderrahimlach.tag.TagManager;
import com.abderrahimlach.tag.TagMenu;
import org.bukkit.entity.Player;

/**
 * @author AbderrahimLach
 */
@CommandAlias("tags|chattags|prefix|prefixes")
public class TagsCommand extends BaseCommand {

    @Dependency
    private TagPlugin plugin;

    @Default
    public void onDefault(Player player){
        TagManager tagManager = plugin.getTagManager();
        if(tagManager.getTags().isEmpty()) {
            ConfigKeys.sendMessage(ConfigKeys.NO_TAGS, player);
            return;
        }

        plugin.getMenuManager().openInventory(player, new TagMenu(plugin));
    }
}
