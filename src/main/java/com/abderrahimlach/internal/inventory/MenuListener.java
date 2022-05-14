package com.abderrahimlach.internal.inventory;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.internal.inventory.manager.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * @author DirectPlan
 */

public class MenuListener implements Listener {

    private final MenuManager menuManager;

    public MenuListener(TagPlugin plugin) {
        menuManager = plugin.getMenuManager();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();

        InventoryUI inventoryUI = menuManager.getInventory(player.getUniqueId());
        if(inventoryUI != null){
            inventoryUI.onClick(event);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        menuManager.removeInventory(player.getUniqueId());
    }
}
