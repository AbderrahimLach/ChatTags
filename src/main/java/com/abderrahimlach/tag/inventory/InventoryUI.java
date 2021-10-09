package com.abderrahimlach.tag.inventory;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.management.MenuManager;
import com.abderrahimlach.management.PlayerManager;
import com.abderrahimlach.utility.Util;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

/**
 * @author AbderrahimLach
 */
@Getter
public abstract class InventoryUI {

    public final TagPlugin plugin;
    private final PlayerManager playerManager;
    private final MenuManager menuManager;
    private Inventory inventory;

    @Setter private String title;
    private final MenuItem[] items;

    public InventoryUI(TagPlugin plugin, String title, int rows){
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.menuManager = plugin.getMenuManager();
        this.title = title;
        this.items = new MenuItem[9 * rows];
    }

    public void setSlot(int slot, MenuItem item){
        this.items[slot] = item;
    }

    public void clearItems() {
        Arrays.fill(this.items, null);
    }

    public MenuItem getItem(int slot){
        return this.items[slot];
    }

    public void onClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        MenuItem item = getItem(slot);
        event.setCancelled(true);
        if(item == null || !item.isClickable()){
            return;
        }
        Player clicker = (Player) event.getWhoClicked();
        item.performAction(item, clicker);
    }

    public Inventory buildInventory(){
        this.inventory = Bukkit.createInventory(null, this.items.length, Util.translateMessage(this.title));
        for(int i = 0; i < this.items.length; i++){
            MenuItem button = this.items[i];
            if(button == null) continue;
            this.inventory.setItem(i, button.getItem());
        }
        return inventory;
    }

    public void updateInventory() {
        for(int i = 0; i < this.items.length; i++) {
            MenuItem button = this.items[i];
            if(button == null) continue;
            this.inventory.setItem(i, button.getItem());
        }
    }

    public abstract Inventory build(Player player);

    public void open(Player player){
        Inventory inventory = build(player);
        player.openInventory(inventory);
        this.menuManager.addInventory(player.getUniqueId(), this);
    }
}