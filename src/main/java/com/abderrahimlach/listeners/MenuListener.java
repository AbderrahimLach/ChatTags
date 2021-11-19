package com.abderrahimlach.listeners;

import com.abderrahimlach.TagPlugin;
import com.abderrahimlach.management.MenuManager;
import com.abderrahimlach.tag.inventory.InventoryUI;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * @author AbderrahimLach
 */
@Data
public class MenuListener implements Listener {

    private final MenuManager menuManager;

    @EventHandler
    public void onClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        InventoryUI inventoryUI = this.menuManager.getInventory(player.getUniqueId());
        if(inventoryUI != null){
            inventoryUI.onClick(event);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        this.menuManager.removeInventory(player.getUniqueId());
    }
}
