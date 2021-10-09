package com.abderrahimlach.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.config.ConfigKeys;
import com.abderrahimlach.management.TagManager;
import com.abderrahimlach.tag.inventory.InventoryUI;
import com.abderrahimlach.tag.TagMenu;
import org.bukkit.entity.Player;

/**
 * @author AbderrahimLach
 */
@CommandAlias("tags|chattags|prefix|prefixes")
public class TagsCommand extends BaseCommand {

    private final TagPlugin plugin;
    private final TagManager tagManager;

    public TagsCommand(TagPlugin plugin){
        this.plugin = plugin;
        this.tagManager = plugin.getTagManager();
    }

    @Default
    public void onDefault(Player player){
        if(this.tagManager.getTags().isEmpty()) {
            ConfigKeys.sendMessage(ConfigKeys.NO_TAGS, player);
            return;
        }
        InventoryUI menu = new TagMenu(this.plugin);
        menu.open(player);
    }
}
