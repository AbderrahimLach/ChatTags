package com.abderrahimlach.internal.inventory;

import com.abderrahimlach.internal.inventory.manager.MenuManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.function.Function;

/**
 * @author DirectPlan
 */
@Getter
public abstract class InventoryUI {

    private final MenuManager menuManager;
    private Inventory inventory;

    @Setter private String title;
    private final MenuItem[] items;

    public InventoryUI(MenuManager menuManager, String title, int rows){
        this.menuManager = menuManager;
        this.title = title;
        items = new MenuItem[9 * rows];
    }

    public void setSlot(int slot, MenuItem item){
        items[slot] = item;
        item.setSlot(slot);
    }

    public void clearItems() {
        Arrays.fill(items, null);
    }

    public MenuItem getItem(int slot){
        return items[slot];
    }

    public void onClick(InventoryClickEvent event) {
        Player clicker = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if(slot < 0) return;
        MenuItem item = getItem(slot);
        if(item == null) return;

        if(item.isCancelAction()) {
            event.setCancelled(true);
        }
        if(!item.isClickable()){
            return;
        }
        item.performAction(item, clicker);
    }

    public Inventory buildInventory(){
        String coloredTitle = ChatColor.translateAlternateColorCodes('&', title);
        int size = items.length;
        this.inventory = Bukkit.createInventory(null, size, coloredTitle);
        for(int i = 0; i < size; i++){
            MenuItem button = items[i];
            if(button == null) continue;
            this.inventory.setItem(i, button.getItem());
        }
        return inventory;
    }

    public void updateSlot(int slot, Function<MenuItem, MenuItem> itemFunction) {
        MenuItem item = getItem(slot);
        if(item == null) return;
        setSlot(slot, itemFunction.apply(item));

        updateInventory();
    }


    public void updateInventory() {
        for(int i = 0; i < items.length; i++) {
            MenuItem button = items[i];
            if(button == null || !button.isClickable()) continue;
            inventory.setItem(i, button.getItem());
        }
    }

    public String getInventoryId() {
        return "Inventory";
    }

    public abstract Inventory build(Player player);

    public void open(Player player){
        Inventory inventory = build(player);
        player.openInventory(inventory);

        menuManager.addInventory(player.getUniqueId(), this);
    }
}