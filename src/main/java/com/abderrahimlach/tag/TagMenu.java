package com.abderrahimlach.tag;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.api.events.PlayerEquippedTagEvent;
import com.abderrahimlach.api.events.PlayerUnequippedTagEvent;
import com.abderrahimlach.internal.config.ConfigKeys;
import com.abderrahimlach.internal.config.replacement.Replacement;
import com.abderrahimlach.internal.inventory.ActionableItem;
import com.abderrahimlach.internal.inventory.MenuItem;
import com.abderrahimlach.internal.inventory.PaginatedMenu;
import com.abderrahimlach.player.PlayerData;
import com.abderrahimlach.player.PlayerManager;
import com.abderrahimlach.utility.PluginUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author AbderrahimLach
 */
public class TagMenu extends PaginatedMenu<Tag> {

    private final TagManager tagManager;
    private final PlayerManager playerManager;

    public TagMenu(TagPlugin plugin) {
        super(plugin.getMenuManager(), null,4, 27);
        tagManager = plugin.getTagManager();
        playerManager = plugin.getPlayerManager();
    }

    @Override
    public Inventory build(Player player) {
        MenuItem glass = new MenuItem(Material.STAINED_GLASS_PANE, "&c", 15);

        for(int slot = 0; slot < 9; slot++){
            this.setSlot(slot, glass);
        }
        ActionableItem action = (item, clicker) -> {
            PlayerData playerData = playerManager.getPlayer(clicker.getUniqueId());
            String tagName = (String) item.getItemKey();
            Tag tag = playerData.getTag(tagName);
            if(tag == null){
                ConfigKeys.sendMessage(ConfigKeys.TAG_NOT_OWNED_PLAYER, player);
                return;
            }
            String coloredTag = PluginUtility.translateMessage(tag.getDisplayName());
            String playerTagInfoMessage;
            if(playerData.getEquippedTag() != tag){
                PlayerEquippedTagEvent equippedTagEvent = new PlayerEquippedTagEvent(playerData, tag);
                equippedTagEvent.call();
                if(equippedTagEvent.isCancelled()) {
                    return;
                }
                playerData.setEquippedTag(tag);
                playerTagInfoMessage = ConfigKeys.GUI_TAG_INFO_UNEQUIP.getValue();
                String message = ConfigKeys.TAG_EQUIPPED.getValue(new Replacement("tag", coloredTag));
                player.sendMessage(message);
            } else {
                PlayerUnequippedTagEvent unequippedTagEvent = new PlayerUnequippedTagEvent(playerData, tag);
                unequippedTagEvent.call();
                if(unequippedTagEvent.isCancelled()) {
                    return;
                }
                playerData.setEquippedTag(null);
                playerTagInfoMessage = ConfigKeys.GUI_TAG_INFO_EQUIP.getValue();
                String message = ConfigKeys.TAG_UNEQUIPPED.getValue(new Replacement("tag", coloredTag));
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
        PlayerData playerData = playerManager.getPlayer(player.getUniqueId());
        Comparator<Tag> comparator = Comparator.comparing(tag -> playerData.getTags().containsValue(tag));
        tags.sort(comparator.reversed());
        for(Tag tag : tags){
            String prefix = tag.getPrefix() != null ? tag.getPrefix() : "&cN/A";
            String tagDisplayName = ConfigKeys.GUI_TAG_DISPLAY_NAME.getValue(new Replacement("tag", tag.getDisplayName()));

            String playerTagInfoMessage = ConfigKeys.GUI_TAG_INFO_NOT_OWNED.getValue();
            if(playerData.hasTag(tag.getName())) {
                playerTagInfoMessage = ConfigKeys.GUI_TAG_INFO_EQUIP.getValue();
                if(playerData.getEquippedTag() == tag) {
                    playerTagInfoMessage = ConfigKeys.GUI_TAG_INFO_UNEQUIP.getValue();
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
