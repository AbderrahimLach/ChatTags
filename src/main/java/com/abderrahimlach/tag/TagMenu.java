package com.abderrahimlach.tag;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.api.events.PlayerEquippedTagEvent;
import com.abderrahimlach.api.events.PlayerUnequippedTagEvent;
import com.abderrahimlach.config.ConfigKeys;
import com.abderrahimlach.config.replacement.Replacement;
import com.abderrahimlach.data.PlayerData;
import com.abderrahimlach.management.PlayerManager;
import com.abderrahimlach.management.TagManager;
import com.abderrahimlach.tag.inventory.ActionableItem;
import com.abderrahimlach.tag.inventory.MenuItem;
import com.abderrahimlach.tag.inventory.PaginatedMenu;
import com.abderrahimlach.utility.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author AbderrahimLach
 */
public class TagMenu extends PaginatedMenu<Tag> {

    private final TagManager tagManager;

    public TagMenu(TagPlugin plugin) {
        super(plugin, null, 4, 27);
        this.tagManager = plugin.getTagManager();
    }

    @Override
    public Inventory build(Player player) {
        MenuItem glass = new MenuItem(Material.STAINED_GLASS_PANE, "&c", 15);

        for(int slot = 0; slot < 9; slot++){
            this.setSlot(slot, glass);
        }
        PlayerManager playerManager = plugin.getPlayerManager();
        ActionableItem action = (item, clicker) -> {
            PlayerData playerData = playerManager.getPlayer(clicker.getUniqueId());
            String tagName = item.getItemKey();
            Tag tag = playerData.getTag(tagName);
            if(tag == null){
                ConfigKeys.sendMessage(ConfigKeys.TAG_NOT_OWNED_PLAYER, player);
                return;
            }
            String coloredTag = Util.translateMessage(tag.getDisplayName());
            String playerTagInfoMessage;
            if(playerData.getEquippedTag() != tag){
                PlayerEquippedTagEvent equippedTagEvent = new PlayerEquippedTagEvent(playerData, tag);
                equippedTagEvent.call();
                if(equippedTagEvent.isCancelled()) {
                    return;
                }
                playerData.setEquippedTag(tag);
                playerTagInfoMessage = ConfigKeys.GUI_TAG_INFO_UNEQUIP.getString();
                String message = ConfigKeys.TAG_EQUIPPED.getString(new Replacement("tag", coloredTag));
                player.sendMessage(message);
            } else {
                PlayerUnequippedTagEvent unequippedTagEvent = new PlayerUnequippedTagEvent(playerData, tag);
                unequippedTagEvent.call();
                if(unequippedTagEvent.isCancelled()) {
                    return;
                }
                playerData.setEquippedTag(null);
                playerTagInfoMessage = ConfigKeys.GUI_TAG_INFO_EQUIP.getString();
                String message = ConfigKeys.TAG_UNEQUIPPED.getString(new Replacement("tag", coloredTag));
                player.sendMessage(message);
            }
            List<String> tagLore = ConfigKeys.GUI_TAG_LORE.getStringList(
                    new Replacement("tag_prefix", tag.getPrefix()),
                    new Replacement("tag_player_info", playerTagInfoMessage)
            );
            item.setLore(tagLore);
            this.updateInventory();
        };
        List<Tag> tags = this.getCurrentPageList();
        AtomicInteger currentIndex = new AtomicInteger(9);
        PlayerData playerData = this.getPlayerManager().getPlayer(player.getUniqueId());
        for(Tag tag : tags){
            String prefix = tag.getPrefix() != null ? tag.getPrefix() : "&cN/A";
            String tagDisplayName = ConfigKeys.GUI_TAG_DISPLAY_NAME.getString(new Replacement("tag", tag.getDisplayName()));

            String playerTagInfoMessage = ConfigKeys.GUI_TAG_INFO_NOT_OWNED.getString();
            if(playerData.hasTag(tag.getName())) {
                playerTagInfoMessage = ConfigKeys.GUI_TAG_INFO_EQUIP.getString();
                if(playerData.getEquippedTag() == tag) {
                    playerTagInfoMessage = ConfigKeys.GUI_TAG_INFO_UNEQUIP.getString();
                }
            }
            List<String> tagLore = ConfigKeys.GUI_TAG_LORE.getStringList(
                    new Replacement("tag_prefix", prefix),
                    new Replacement("tag_player_info", playerTagInfoMessage)
            );

            MenuItem item = new MenuItem(Material.NAME_TAG, tagDisplayName, action);
            item.setItemKey(tag.getName());
            item.setLore(tagLore);
            this.setSlot(currentIndex.getAndIncrement(), item);
        }
        return super.build(player);
    }

    @Override
    public Collection<Tag> getList() {
        return tagManager.getTags();
    }
}
