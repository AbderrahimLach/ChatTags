package com.abderrahimlach.internal.inventory.manager;

import com.abderrahimlach.internal.inventory.InventoryUI;
import com.abderrahimlach.player.PlayerData;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author AbderrahimLach
 */
public class MenuManager {

    @Getter private final Map<UUID, InventoryUI> inventories = new HashMap<>();

    public InventoryUI getInventory(UUID uuid){
        return inventories.get(uuid);
    }

    public void removeInventory(UUID uuid){
        this.inventories.remove(uuid);
    }

    public void addInventory(UUID uuid, InventoryUI inventoryUI){
        inventories.put(uuid, inventoryUI);
    }

    public void openInventory(PlayerData playerData, InventoryUI inventoryUI) {
        Player player = playerData.getPlayer();
        openInventory(player, inventoryUI);
    }

    public void openInventory(Player player, InventoryUI inventoryUI) {
        inventoryUI.open(player);
//        addInventory(player.getUniqueId(), inventoryUI);
    }
}
